#!/usr/bin/env bash
# Render một JAR J2ME ở khung 320x240 (giả lập màn E72) ra loạt PNG — KHÔNG cần điện thoại.
# Dựa trên MicroEmulator headless + J2SEDisplayGraphics đã vá clip (giống KVM thật).
#
#   ./render.sh <game.jar> <outDir> "<script>"
# script = các bước cách nhau bởi dấu phẩy:
#   wait:<ms>    chờ
#   shot:<name>  chụp khung hiện tại (crop 320x240 góc trên-trái)
#   key:<code>   bấm phím (Nokia keycode: UP=-1 DOWN=-2 LEFT=-3 RIGHT=-4 FIRE=-5,
#                softkey -6/-7, số '0'..'9' = 48..57, '*'=42 '#'=35)
# vd: ./render.sh out/landscape.jar /tmp/shots "wait:7000,key:-5,wait:1500,shot:menu"
set -euo pipefail
JAVA=/opt/homebrew/opt/openjdk@26/bin
HERE="$(cd "$(dirname "$0")" && pwd)"
ME="$HERE/../microemulator.jar"
CLS="$HERE/.cls"

mkdir -p "$CLS"
# biên dịch harness + lớp emulator đã vá (clip drawRGB) nếu chưa có
if [ ! -f "$CLS/Shot.class" ] || [ "$HERE/Shot.java" -nt "$CLS/Shot.class" ]; then
  "$JAVA/javac" -cp "$ME" -d "$CLS" "$HERE/J2SEDisplayGraphics.java" "$HERE/Shot.java"
fi
# patchcls (lớp đã vá) đặt TRƯỚC jar để override; e72/ chứa device.xml 320x240 (render 400x400 rồi crop)
"$JAVA/java" -Djava.awt.headless=false -cp "$CLS:$ME:$HERE" Shot "$1" "$2" "${3:-wait:3000,shot:frame}"
