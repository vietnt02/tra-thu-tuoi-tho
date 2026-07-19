#!/usr/bin/env bash
# Mở cửa sổ giả lập 320x240 trên Mac để CHƠI THỬ game bằng bàn phím — không cần E72.
#
#   ./play.sh [game.jar]     (mặc định: bản landscape mới nhất)
#
# Phím: mũi tên = di chuyển | Enter = chọn/OK | F1,F2 = 2 phím chức năng
#       (hoặc bấm chuột vào nút trên cửa sổ) | phím số 0-9 như bàn phím điện thoại
# Save game giữ lại giữa các lần chạy (work/out/rms/).
set -euo pipefail
JAVA=/opt/homebrew/opt/openjdk@26/bin
HERE="$(cd "$(dirname "$0")" && pwd)"
ME="$HERE/../microemulator.jar"
CLS="$HERE/.cls"
JAR="${1:-$HERE/../../products/quan-va-ti-quay/work/out/landscape.jar}"

# biên dịch lớp emulator đã vá (clip drawRGB như máy thật) nếu chưa có
if [ ! -f "$CLS/org/microemu/device/j2se/J2SEDisplayGraphics.class" ]; then
  mkdir -p "$CLS"
  "$JAVA/javac" -cp "$ME" -d "$CLS" "$HERE/J2SEDisplayGraphics.java" "$HERE/Shot.java"
fi

RMS="$HERE/../../products/quan-va-ti-quay/work/out/rms"
mkdir -p "$RMS"
cd "$RMS"   # microemulator ghi file rms vào cwd
exec "$JAVA/java" -cp "$CLS:$ME" org.microemu.app.Main \
  --rms file --resizableDevice 320 240 "$JAR"
