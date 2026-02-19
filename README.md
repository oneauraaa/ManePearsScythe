<div align="center">

![ManePear's Scythe](https://files.catbox.moe/h579nm.jpg)

# ManePear's Scythe

**Adds the Scythe from ManePear's videos.**

**Huge thanks to [Oasis](https://modrinth.com/user/OasisSucksAtMC) for letting me use his Scythe texture!**

## Features

| Feature | Description |
| :--- | :--- |
| Configurable Recipe | Fully configurable recipe by config. |
| Attributes | Has 12 base damage **_(can 4-shot full neth armor)_** and the cooldown of a sword. |
| Shield Breaker | Disables enemy shields on hit like an axe. |
| No Fall Damage | The wielder takes no fall damage while holding the Scythe. |
| Smash Attacks | Can smash attack like a mace. |
| One-Time Craft **(Optional)** | Limit the Scythe to be crafted only once. |
| Unenchantable | Cannot be enchanted or modified in an anvil. |

## Default Config
</div>

```yml
# ManePearsScythe Configuration
#
# Prefix for all scythe messages (supports & color codes)
prefix: "&5[Scythe]"

# Display name of the Scythe item (supports & color codes)
scythe-name: "&5Scythe"

# If true, the Scythe can only be crafted once. Persists across restarts.
one-time-craftable-only: true

# Internal tracking - do not edit manually (set automatically by the plugin)
scythe-crafted: false

# Resource Pack
#
# URL to the resource pack zip file
# SHA1 hash of the resource pack (used for caching)
resource-pack:
  url: ""
  sha1: ""

# Crafting Recipe
#
# Shape uses a 3x3 grid (row1 = top, row3 = bottom).
# Each character maps to an ingredient below.
# Use a space for empty slots.
#
# Default recipe layout:
#   [   ] [   ] [ N ]
#   [   ] [ H ] [ N ]
#   [ B ] [   ] [ N ]
#
# N = Netherite Ingot, H = Heavy Core, B = Breeze Rod
recipe:
  row1: "  N"
  row2: " HN"
  row3: "B N"
  ingredients:
    N: NETHERITE_INGOT
    H: HEAVY_CORE
    B: BREEZE_ROD
```
<div align="center">

---

## Disclaimer

This plugin is licensed under the **MIT License**.

**The Scythe texture is by [Oasis](https://modrinth.com/user/OasisSucksAtMC) for letting me use his pack!**
</div>




