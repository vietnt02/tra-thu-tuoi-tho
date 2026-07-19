#!/usr/bin/env bash
# Pipeline vá -> đóng gói -> sinh JAD cho Quan va Ti Quay.
# Giữ original/ bất biến; mọi thứ ghi vào work/out/.
#
#   ./make.sh identity     -> tháo f.class rồi nhét lại KHÔNG đổi -> chứng minh
#                             pipeline không tự làm hỏng (Phase 1 success criterion)
#   ./make.sh spike [Y]    -> vá Y splash (mặc định 60) -> spike.jar + spike.jad
#   ./make.sh menu         -> nền menu thành ĐỎ + full-width -> spike-menu.jar
#   ./make.sh landscape    -> reflow khung nhìn thế giới 240x320 -> 320x240
#                             (viewport g.class + ring buffer f.class) -> landscape.jar
set -euo pipefail

JAVA=/opt/homebrew/opt/openjdk@26/bin
HERE="$(cd "$(dirname "$0")" && pwd)"
ORIG="$HERE/../original/quan-va-ti-quay.jar"
OUT="$HERE/out"
STAGE="$OUT/stage"
mode="${1:-spike}"
newY="${2:-60}"

mkdir -p "$OUT"
rm -rf "$STAGE"; mkdir -p "$STAGE"

# Các class đụng tới (mặc định chỉ f.class); landscape đụng f g b d k
CLASSES="f.class"
case "$mode" in
  identity) out="$OUT/identity.jar" ;;          # nhét lại y nguyên
  spike)    out="$OUT/spike.jar" ;;
  menu)     out="$OUT/spike-menu.jar" ;;
  landscape) out="$OUT/landscape.jar"; CLASSES="f.class g.class b.class d.class k.class" ;;
  *) echo "mode: identity | spike [Y] | menu | landscape"; exit 2 ;;
esac

# Tháo các class cần vá (các entry khác giữ nguyên byte)
( cd "$STAGE" && "$JAVA/jar" xf "$ORIG" $CLASSES )

case "$mode" in
  spike) python3 "$HERE/patch-class.py" "$STAGE/f.class" "$STAGE/f.class" splash "$newY" ;;
  menu)  python3 "$HERE/patch-class.py" "$STAGE/f.class" "$STAGE/f.class" menu ;;
  landscape)
    python3 "$HERE/patch-landscape.py" "$STAGE"
    # Vá toạ độ đổi độ dài lệnh (camera/màn tĩnh/softkey/wrap/hằng script)
    "$JAVA/java" -cp "$HERE/../../../tools/asm.jar:$HERE/../../../tools/asm-tree.jar" \
      "$HERE/LandscapePatch.java" "$STAGE"
    ;;
esac

# Đóng gói: copy jar gốc rồi update DUY NHẤT các entry đã vá
# -> các entry + manifest khác byte-identical với bản gốc
cp "$ORIG" "$out"
( cd "$STAGE" && zip -q -X "$out" $CLASSES )

# landscape: preverify (ASM đã đổi độ dài lệnh -> sinh lại StackMap CLDC),
# rồi thay tranh khổ ngang từng màn nếu có file art
if [ "$mode" = "landscape" ]; then
  pre="$OUT/landscape-pre.jar"
  mv "$out" "$pre"
  "$HERE/../../../tools/preverify.sh" "$pre" "$out"
  rm -f "$pre"
  [ -f "$HERE/art-landscape/pack4-blob0-gameplay-320x240.png" ] && \
    python3 "$HERE/patch-pack.py" "$out" 4 0 "$HERE/art-landscape/pack4-blob0-gameplay-320x240.png"
  [ -f "$HERE/art-landscape/pack3-blob0-menu-320x240.png" ] && \
    python3 "$HERE/patch-pack.py" "$out" 3 0 "$HERE/art-landscape/pack3-blob0-menu-320x240.png"
  # vá toạ độ UI trong script data (dialog/story/menu/HUD/popup)
  [ -f "$HERE/script-patches.tsv" ] && \
    python3 "$HERE/script-tool.py" patch "$out" "$HERE/script-patches.tsv"
  # màn chọn màn: dịch 4 nút (rương + con trỏ đọc chung res1030) +40 -> ra giữa
  python3 "$HERE/script-tool.py" shiftx "$out" 1030 40
fi
size=$(stat -f%z "$out")

# Sinh JAD từ manifest gốc + size jar mới
( cd "$STAGE" && "$JAVA/jar" xf "$ORIG" META-INF/MANIFEST.MF )
mf="$STAGE/META-INF/MANIFEST.MF"
jad="${out%.jar}.jad"
{
  grep -E '^MIDlet-Name:|^MIDlet-Version:|^MIDlet-Vendor:|^MicroEdition-Configuration:|^MicroEdition-Profile:|^MIDlet-1:' "$mf" | tr -d '\r'
  echo "MIDlet-Jar-URL: $(basename "$out")"
  echo "MIDlet-Jar-Size: $size"
} > "$jad"

echo "----"
echo "OUT : $out ($size bytes)"
echo "JAD : $jad"
"$JAVA/javap" -classpath "$out" -verbose f 2>/dev/null | grep -E 'major version|minor version' | sed 's/^/  class /'
