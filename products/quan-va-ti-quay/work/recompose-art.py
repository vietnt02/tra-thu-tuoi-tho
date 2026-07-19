#!/usr/bin/env python3
"""Dựng tranh khổ NGANG 320x240 từ tranh gốc dọc 240x320 — GIỮ nguyên art gốc,
chỉ nới nền đồng nhất. Kỹ thuật: (1) nới bề ngang 240->320 bằng nhân bản cột mép
(nền tối + rào tre kéo dài tự nhiên, không đụng logo/nhân vật ở giữa); (2) fit cao
320->240 bằng co từng vùng (title / khung giấy / đáy) để "cửa sổ giấy" rơi đúng chỗ
game vẽ nội dung động lên trên.

Hiện làm: khung gameplay (pack '4' blob 0). Thêm màn khác theo cùng công thức.
Dùng:  recompose-art.py   (đọc art-extract/, ghi art-landscape/)
"""
import os
from PIL import Image, ImageEnhance, ImageFilter

HERE = os.path.dirname(os.path.abspath(__file__))
SRC = os.path.join(HERE, "art-extract")
DST = os.path.join(HERE, "art-landscape")

def widen_edge(im, target_w=320):
    """Nới ngang bằng nhân bản cột trái/phải (nền mép đồng nhất)."""
    w, h = im.size
    pad = (target_w - w) // 2
    out = Image.new("RGB", (target_w, h))
    out.paste(im, (pad, 0))
    out.paste(im.crop((0, 0, 1, h)).resize((pad, h)), (0, 0))
    out.paste(im.crop((w - 1, 0, w, h)).resize((target_w - pad - w, h)), (pad + w, 0))
    return out

def gameplay_frame():
    """Khung gameplay khổ ngang: GIỮ nguyên tranh gốc, KHÔNG co méo.
    Nới ngang 240->320 (edge-replicate) rồi CẮT bỏ 80px đáy (village trang trí) ->
    khung giấy gốc (y88-232) đứng yên -> nội dung game (y125-215) rơi đúng trong khung,
    không mối nối, đúng nét gốc. (So với co-từng-vùng: sạch hơn hẳn — đã chấm trên harness.)"""
    im = widen_edge(Image.open(os.path.join(SRC, "res4_png0_240x320.png")).convert("RGB"))
    return im.crop((0, 0, 320, 240))

def blur_backdrop(col, target_w=320):
    """Nới ngang bằng nền 'wallpaper': chính cột art phóng to phủ kín khung,
    làm mờ + tối nhẹ, rồi đặt cột art gốc vào giữa. Hai cánh cùng tông màu với
    art nên nhìn có chủ đích — thay cho widen_edge (nhân bản cột mép) vốn ra
    vệt sọc nhòe khi mép ảnh không đồng nhất (lá cây, viền card)."""
    W, H = col.size
    bg = col.resize((target_w, round(H * target_w / W)), Image.LANCZOS)
    top = (bg.height - H) // 2
    bg = bg.crop((0, top, target_w, top + H)).filter(ImageFilter.GaussianBlur(7))
    bg = ImageEnhance.Brightness(bg).enhance(0.88)
    bg.paste(col, ((target_w - W) // 2, 0))
    return bg

def menu_scene():
    """Menu khổ ngang v4 — giữ NGUYÊN VẸN tranh gốc, scale đều cả bức 320->240 cao
    (75%, LANCZOS — art cartoon nét mượt nên vẫn sạch), đặt giữa trên blur_backdrop.
    Các bản trước cắt-ghép banner + nhân vật để né scale, nhưng khổ 240 không đủ chỗ
    (banner 106 + nhân vật nguyên đầu 181 = 287) -> ranh cắt y191 chém mất đầu
    nhân vật chính (đầu bắt đầu từ y~133). Client đã báo lỗi này 2 lần."""
    im = Image.open(os.path.join(SRC, "res3_png0_240x320.png")).convert("RGB")
    card = im.resize((180, 240), Image.LANCZOS)
    return blur_backdrop(card, 320)

if __name__ == "__main__":
    os.makedirs(DST, exist_ok=True)
    gameplay_frame().save(os.path.join(DST, "pack4-blob0-gameplay-320x240.png"))
    print("[OK] art-landscape/pack4-blob0-gameplay-320x240.png")
    menu_scene().save(os.path.join(DST, "pack3-blob0-menu-320x240.png"))
    print("[OK] art-landscape/pack3-blob0-menu-320x240.png")
