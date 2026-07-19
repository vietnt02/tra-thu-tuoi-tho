#!/usr/bin/env bash
# Sinh trang tải tĩnh cấp PROJECT từ nguồn duy nhất là products/ — KHÔNG chép tay gì vào site/.
# Với mỗi game (products/<slug>/):
#   release/*.jar        — các bản build theo độ phân giải (bắt buộc)
#   shots/*.png          — ảnh chụp màn hình giới thiệu (tuỳ chọn)
#   description.txt      — 1 đoạn mô tả ngắn (tuỳ chọn)
# Icon + tên game đọc từ manifest trong jar. Kết quả sinh vào site/ (build output, không commit).
# Deploy: GitHub Actions chạy script này rồi đẩy site/ lên GitHub Pages (.github/workflows/deploy-pages.yml).
#   ./make-site.sh
set -euo pipefail
ROOT="$(cd "$(dirname "$0")" && pwd)"
SITE="$ROOT/site"
rm -rf "$SITE"; mkdir -p "$SITE"

manifest_field(){ unzip -p "$1" META-INF/MANIFEST.MF 2>/dev/null | tr -d '\r' | awk -F': ' -v k="$2" '$1==k{print $2; exit}'; }
file_kb(){ echo $(( ($(wc -c < "$1" | tr -d ' ') + 512) / 1024 )); }

sections=""
for prod in "$ROOT"/products/*/; do
  slug="$(basename "$prod")"
  shopt -s nullglob; jars=("$prod"release/*.jar); shopt -u nullglob
  [ ${#jars[@]} -gt 0 ] || continue

  gdir="$SITE/games/$slug"; mkdir -p "$gdir"

  title="$(manifest_field "${jars[0]}" MIDlet-Name)"
  [ -n "$title" ] || title="$slug"
  # vendor.txt (tuỳ chọn) ghi đè MIDlet-Vendor trong manifest — dùng khi manifest
  # ghi tên site reup thay vì tác giả thật
  vendor="$(manifest_field "${jars[0]}" MIDlet-Vendor)"
  [ -f "${prod}vendor.txt" ] && vendor="$(cat "${prod}vendor.txt")"
  version="$(manifest_field "${jars[0]}" MIDlet-Version)"
  meta="$vendor"; [ -n "$version" ] && meta="$meta · v$version"

  # icon: lấy từ trong jar theo đường dẫn khai báo ở manifest
  icon_html=""
  iconpath="$(manifest_field "${jars[0]}" MIDlet-Icon)"; iconpath="${iconpath#/}"
  if [ -n "$iconpath" ] && unzip -p "${jars[0]}" "$iconpath" > "$gdir/icon.png" 2>/dev/null && [ -s "$gdir/icon.png" ]; then
    icon_html="<img class=\"icon\" src=\"./games/$slug/icon.png\" alt=\"\" width=\"56\" height=\"56\">"
  else
    rm -f "$gdir/icon.png"
    icon_html="<div class=\"icon icon-fallback\">🎮</div>"
  fi

  desc_html=""
  if [ -f "${prod}description.txt" ]; then
    desc_html="<p class=\"desc\">$(cat "${prod}description.txt")</p>"
  fi

  shots_html=""
  shopt -s nullglob; shots=("$prod"shots/*.png); shopt -u nullglob
  if [ ${#shots[@]} -gt 0 ]; then
    mkdir -p "$gdir/shots"
    for s in "${shots[@]}"; do
      sbase="$(basename "$s")"
      cp "$s" "$gdir/shots/$sbase"
      shots_html="$shots_html
        <figure class=\"shot\"><img src=\"./games/$slug/shots/$sbase\" alt=\"Ảnh trong game $title\" loading=\"lazy\"></figure>"
    done
    shots_html="<div class=\"shots\">$shots_html
      </div>"
  fi

  rows=""
  for jar in "${jars[@]}"; do
    base="$(basename "$jar")"
    cp "$jar" "$gdir/$base"
    kb="$(file_kb "$jar")"
    res="$(echo "$base" | grep -oE '[0-9]+x[0-9]+' | head -1 || true)"
    orient=""; hint=""
    if [ -n "$res" ]; then
      w="${res%x*}"; h="${res#*x}"
      if [ "$w" -gt "$h" ]; then orient="Màn ngang"; else orient="Màn dọc"; fi
      case "$res" in
        320x240) hint="Nokia E72, E63, E71…" ;;
        240x320) hint="Nokia màn dọc phổ thông" ;;
      esac
      res="${w}×${h}"
    else res="—"; fi
    rows="$rows
      <a class=\"dlrow\" href=\"./games/$slug/$base\" download>
        <span class=\"res\">$res</span>
        <span class=\"orient\">$orient${hint:+ <small>$hint</small>}</span>
        <span class=\"size\">$kb KB</span>
        <span class=\"btn\">Tải .jar</span>
      </a>"
  done

  sections="$sections
    <article class=\"game\">
      <div class=\"game-head\">
        $icon_html
        <div>
          <h2>$title</h2>
          <div class=\"meta\">$meta</div>
        </div>
      </div>
      $desc_html
      $shots_html
      <div class=\"dl\">$rows
      </div>
    </article>"
done

cat > "$SITE/index.html" <<HTML
<!doctype html>
<html lang="vi">
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Trả Thù Tuổi Thơ — game Nokia mod đúng màn hình</title>
<meta name="description" content="Game J2ME tuổi thơ mod lại cho đúng kích thước màn hình Nokia cổ. Tải file .jar, chép vào thẻ nhớ là chơi.">
<style>
  :root{
    --bg:#f5f0e6; --dot:#e9e1d0; --card:#fffdf8; --ink:#292018; --muted:#92826f;
    --accent:#d9541f; --accent-ink:#fff; --line:#e7dcc8; --chip:#f6ede0; --bezel:#221d17; --phone:#221d17;
  }
  @media (prefers-color-scheme:dark){
    :root{
      --bg:#16130f; --dot:#1e1a14; --card:#211c16; --ink:#f2e9db; --muted:#a2937f;
      --accent:#ff7a45; --accent-ink:#251208; --line:#383026; --chip:#2b251d; --bezel:#0d0b08; --phone:#463c2f;
    }
  }
  *{ box-sizing:border-box; } html,body{ margin:0; }
  body{
    background:var(--bg);
    background-image:radial-gradient(var(--dot) 1px, transparent 1.5px);
    background-size:22px 22px;
    color:var(--ink);
    font:16px/1.55 -apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif;
    padding:28px 16px 48px;
  }
  .wrap{ max-width:680px; margin:0 auto; }

  header{ text-align:center; margin:8px 0 30px; }
  .logo{ display:inline-block; margin-bottom:10px; }
  h1{ font-size:1.9rem; letter-spacing:-.02em; margin:0 0 6px; }
  h1 span{ color:var(--accent); }
  .sub{ color:var(--muted); font-size:.95rem; max-width:40ch; margin:0 auto; }

  .game{
    background:var(--card); border:1px solid var(--line); border-radius:18px;
    padding:22px 22px 18px; margin:0 0 26px;
    box-shadow:0 1px 2px rgba(0,0,0,.04), 0 10px 30px -18px rgba(0,0,0,.18);
  }
  .game-head{ display:flex; align-items:center; gap:14px; }
  .icon{
    width:56px; height:56px; border-radius:14px; flex:0 0 auto;
    image-rendering:pixelated; border:1px solid var(--line); background:var(--chip);
  }
  .icon-fallback{ display:flex; align-items:center; justify-content:center; font-size:26px; }
  h2{ font-size:1.35rem; margin:0; letter-spacing:-.01em; }
  .meta{ color:var(--muted); font-size:.85rem; margin-top:2px; }
  .desc{ color:var(--muted); font-size:.93rem; margin:14px 0 0; }

  .shots{
    display:flex; gap:12px; overflow-x:auto; margin:16px -6px 0; padding:2px 6px 10px;
    scroll-snap-type:x proximity; -webkit-overflow-scrolling:touch;
  }
  .shot{
    flex:0 0 auto; margin:0; scroll-snap-align:start;
    background:var(--bezel); padding:8px; border-radius:12px;
  }
  .shot img{ display:block; height:190px; width:auto; border-radius:5px; }
  @media (max-width:520px){ .shot img{ height:150px; } }

  .dl{ display:flex; flex-direction:column; gap:8px; margin-top:14px; }
  .dlrow{
    display:flex; align-items:center; gap:14px; text-decoration:none; color:inherit;
    border:1px solid var(--line); border-radius:12px; padding:10px 14px;
    transition:border-color .12s ease, background .12s ease;
  }
  .dlrow:hover{ border-color:var(--accent); background:var(--chip); }
  .res{
    font-family:ui-monospace,SFMono-Regular,Menlo,Consolas,monospace;
    font-weight:700; font-size:.95rem; background:var(--chip);
    border:1px solid var(--line); border-radius:8px; padding:4px 9px; white-space:nowrap;
  }
  .orient{ flex:1; min-width:0; font-size:.92rem; }
  .orient small{ display:block; color:var(--muted); font-size:.8rem; }
  .size{ color:var(--muted); font-size:.83rem; white-space:nowrap;
         font-family:ui-monospace,SFMono-Regular,Menlo,Consolas,monospace; }
  .btn{
    background:var(--accent); color:var(--accent-ink); font-weight:650; font-size:.87rem;
    padding:8px 15px; border-radius:9px; white-space:nowrap;
  }
  @media (max-width:440px){ .size{ display:none; } }

  footer{ text-align:center; color:var(--muted); font-size:.8rem; margin-top:30px; }
</style>
</head>
<body>
  <div class="wrap">
    <header>
      <svg class="logo" width="44" height="64" viewBox="0 0 22 32" aria-hidden="true">
        <rect x="1" y="1" width="20" height="30" rx="4" fill="var(--phone)"/>
        <rect x="4" y="5" width="14" height="11" rx="1.5" fill="var(--accent)"/>
        <rect x="6" y="7" width="6" height="2" rx="1" fill="var(--card)" opacity=".85"/>
        <circle cx="7" cy="21" r="1.6" fill="var(--muted)"/><circle cx="11" cy="21" r="1.6" fill="var(--muted)"/><circle cx="15" cy="21" r="1.6" fill="var(--muted)"/>
        <circle cx="7" cy="25.5" r="1.6" fill="var(--muted)"/><circle cx="11" cy="25.5" r="1.6" fill="var(--muted)"/><circle cx="15" cy="25.5" r="1.6" fill="var(--muted)"/>
      </svg>
      <h1>Trả Thù <span>Tuổi Thơ</span></h1>
      <p class="sub">Game Nokia tuổi thơ mod lại cho đúng kích thước màn hình — không méo hình, không xoay máy. Chọn bản khớp độ phân giải máy bạn.</p>
    </header>
    ${sections}
    <footer>Cập nhật: $(date +%d/%m/%Y)</footer>
  </div>
</body>
</html>
HTML

echo "[OK] $SITE/index.html"
