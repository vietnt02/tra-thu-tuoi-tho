#!/usr/bin/env bash
# Preverify lại 1 JAR J2ME sau khi vá ĐỔI ĐỘ DÀI lệnh (ASM chèn/xoá lệnh, đổi
# bipush->sipush...). Sinh lại StackMap CLDC hợp lệ (class version giữ 45) để E72
# KVM chấp nhận. Đây là "đường vá khó" mở khoá mọi chỉnh toạ độ (căn giữa/nới content).
#
#   ./preverify.sh <in.jar> <out.jar>
#
# Thư viện CLDC/MIDP: lấy từ microemulator.jar (API javax.microedition) + java.base
# của JDK21 (ProGuard 7.6.1 đọc được class <= Java23; JDK26 quá mới nên KHÔNG dùng).
set -euo pipefail
HERE="$(cd "$(dirname "$0")" && pwd)"
JAVA26=/opt/homebrew/opt/openjdk@26/bin
JH21=/opt/homebrew/opt/openjdk@21/libexec/openjdk.jdk/Contents/Home
PG="$HERE/proguard.jar"
ME="$HERE/microemulator.jar"
in="$1"; out="$2"

[ -f "$PG" ] || { echo "Thiếu $PG (tải proguard.jar 7.6.x vào tools/)"; exit 1; }

cfg=$(mktemp)
cat > "$cfg" <<EOF
-injars $in(!META-INF/MANIFEST.MF)
-outjars $out
-libraryjars $ME
-libraryjars $JH21/jmods/java.base.jmod(!module-info.class)
-dontshrink
-dontoptimize
-dontobfuscate
-microedition
-keepattributes *
-keep class ** { *; }
-ignorewarnings
-dontnote
EOF
"$JAVA26/java" -jar "$PG" @"$cfg"
rm -f "$cfg"
# nhét lại manifest gốc (ProGuard bỏ) từ input
tmpd=$(mktemp -d); ( cd "$tmpd" && "$JAVA26/jar" xf "$in" META-INF/MANIFEST.MF && zip -q "$out" META-INF/MANIFEST.MF )
rm -rf "$tmpd"
echo "[preverify] -> $out"
