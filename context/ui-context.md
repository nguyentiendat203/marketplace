# UI Context

## Theme

The design language operates on a strict **Two-Canvas System** with shared typographic DNA but divergent polarities. It split into two distinct tracks that must never be blended on the same page:

1. **Marketing Track (Cinematic Dark):** A premium, print-magazine-like dark experience. Pure black backgrounds, heavy use of negative space, full-bleed photography, and thin editorial typography.
2. **Transactional Track (Light/Cream):** A high-density light experience for pricing tables, signup flows, and checkout. It uses off-white/cream canvases and introduces vibrant green accents to signal commerce and growth.

## Colors

All components must use these tokens mapped to CSS custom properties. No hardcoded hex values are allowed.

### 1. Marketing Track (Cinematic Dark Canvas)

| Role            | CSS Variable          | Value     | Description                                           |
| --------------- | --------------------- | --------- | ----------------------------------------------------- |
| Page background | `--bg-base`           | `#000000` | `{colors.canvas-night}` - Pure black canvas           |
| Surface         | `--bg-surface`        | `#0a0a0a` | `{colors.canvas-night-elevated}` - Dark cards         |
| Surface Accent  | `--bg-surface-accent` | `#1e2c31` | `{colors.surface-elevated-dark}` - Teal-shifted depth |
| Primary text    | `--text-primary`      | `#ffffff` | `{colors.on-primary}` - Pure white text               |
| Muted text      | `--text-muted`        | `#a1a1aa` | `{colors.shade-40}` - Secondary text on dark          |
| Border          | `--border-default`    | `#1e2c31` | `{colors.hairline-dark}` - 1px visible dark chrome    |
| Link Muted 1    | `--link-cool-1`       | `#9dabad` | Quiet footer/tertiary link color                      |
| Link Muted 2    | `--link-cool-2`       | `#9797a2` | Muted link tone                                       |

### 2. Transactional Track (Light/Cream Canvas)

| Role             | CSS Variable         | Value     | Description                                               |
| ---------------- | -------------------- | --------- | --------------------------------------------------------- |
| Page background  | `--bg-base`          | `#fbfbf5` | `{colors.canvas-cream}` - Warm off-white canvas           |
| Alt Background   | `--bg-base-light`    | `#ffffff` | `{colors.canvas-light}` - Pure white surface              |
| Primary text     | `--text-primary`     | `#000000` | `{colors.ink}` - Deep black text                          |
| Secondary text   | `--text-secondary`   | `#71717a` | `{colors.shade-50}` - Secondary text on light             |
| Muted text       | `--text-muted`       | `#a1a1aa` | `{colors.shade-40}` - Tertiary text on light              |
| Primary accent   | `--accent-primary`   | `#c1fbd4` | `{colors.aloe-10}` - Mint green for featured tier / CTA   |
| Secondary accent | `--accent-secondary` | `#d4f9e0` | `{colors.pistachio-10}` - Softer green for section bands  |
| Border           | `--border-default`   | `#e4e4e7` | `{colors.hairline-light}` - 1px light card/table dividers |
| Tag Background   | `--bg-tag-neutral`   | `#d4d4d8` | `{colors.shade-30}` - Neutral chip fill                   |

## Typography

> **Universal Requirement:** The OpenType `ss03` stylistic set must be enabled globally (`font-feature-settings: "ss03"`) to enforce geometric character alterations (lowercase a, g, and single-story numerals).

| Role      | Font                        | Variable      | Key Rules                                                                                                                                 |
| --------- | --------------------------- | ------------- | ----------------------------------------------------------------------------------------------------------------------------------------- |
| Display   | `Neue Haas Grotesk Display` | `--font-disp` | **Must always render at thin weights (330-500). Never use 400+ for large titles.** Large displays (96px) require `letter-spacing: 2.4px`. |
| UI text   | `Inter Variable`            | `--font-sans` | Used for body (420), captions (500), and strong text (550).                                                                               |
| Code/mono | `ui-monospace`              | `--font-mono` | System mono for code blocks and rare technical eyebrows.                                                                                  |

## Border Radius

The system uses a strict radius scale. **Rounded rectangles do not exist for buttons**—all buttons must be pills.

| Context           | Class           | Token mapping                                                      |
| ----------------- | --------------- | ------------------------------------------------------------------ |
| Inline / small UI | `rounded-sm`    | `4px` (`{rounded.xs}`) for inputs and hairline tags.               |
| Image containers  | `rounded-[5px]` | `5px` (`{rounded.sm}`) for small image frames.                     |
| Form inputs/video | `rounded-md`    | `8px` (`{rounded.md}`) for inputs and video frames.                |
| Cards / panels    | `rounded-lg`    | `12px` (`{rounded.lg}`) for pricing and feature cards.             |
| Asymmetric frames | `rounded-xl`    | `20px` (`{rounded.xl}`) top-only on cinematic cards.               |
| Interactive CTAs  | `rounded-full`  | `9999px` (`{rounded.pill}`) **Strictly enforced for ALL buttons.** |

## Component Library

Custom Tailwind components layered with a strict token design vocabulary.

- **Primary Pill Button (`button-primary-pill`):** `rounded-full bg-black text-white px-6 py-3 text-base`. On pressed state, background shifts to `#3f3f46` (`{colors.shade-70}`).
- **Cinematic Hero Button (`button-outline-on-dark`):** `rounded-full bg-transparent border-2 border-white text-white px-6 py-3`.
- **Light Track Button (`button-outline-on-light`):** `rounded-full bg-white border border-black text-black px-6 py-3`.
- **Featured Tier Button (`button-aloe-pill`):** `rounded-full bg-[#c1fbd4] text-black px-6 py-3`. Reserved for "Start free trial".

## Layout Patterns

- **Cinematic Marketing Band:** Full-bleed, edge-to-edge photography with giant text (`{typography.display-xxl}` at 96px). Extreme vertical whitespace air (128px–192px padding between blocks). No overlapping text over images; type lives in clean negative space. Single CTA per band.
- **Transactional Table Layout:** High-density structured content. Vertical padding collapses to ~48px. Uses a responsive collapsing grid: 4-up columns on Desktop (≥1024px) $\rightarrow$ 2-up on Tablet $\rightarrow$ 1-up stack on Mobile ($<768\text{px}$).
- **The Halo Elevation Card:** Standard pricing cards are flat with a 1px border. The featured card uses Level 3 Elevation: a layered shadow stack (`0 8px 8px rgba(0,0,0,0.1), 0 4px 4px rgba(0,0,0,0.1), ...`) creating a soft, paper-like halo effect on light surfaces.
- **Global Navigation Bar:** Top bar layout. Logo on left, nav items centered, dual pill-shaped CTAs pinned to the right. Follows canvas polarity (pure black on marketing, pure light on transactional).

## Icons

- **Style:** Wordmark-driven styling preferred. When icons are required, use clean stroke-based icons only (e.g., Lucide React).
- **Sizing:** `h-4 w-4` (16px) for inline text alignments and button labels.
- **Customer Logos:** Rendered as grayscale wordmarks at uniform height (24px–32px) arranged in a flat, horizontal strip. No colorful tech logos allowed.
