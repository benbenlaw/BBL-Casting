---
navigation:
    title: Logistics
    icon: 'casting:controller'
    parent: index.md
    position: 5
item_ids:
    - 'casting:controller'
    - 'casting:mixer'
    - 'casting:solidifier'

---

# Logistics

## Logistics

Casting blocks will push and pull fluids to each other.

## Directions
- The Controller will push fluids from its outputs to adjacent Solidifiers and Mixers
- The Solidifier will accept fluids from adjacent Controllers and Mixers (Output Tank)
- The Mixer will accept fluids from adjacent Controllers

## Example
<GameScene zoom="3" interactive={true}>
  <ImportStructure src="assets/structures/logistics.nbt" />
</GameScene>
The Controller is pushing to the solidifier on the right and the mixer on the top. The mixer onto is then pushing its output to the solidifier on the right. 

## Pipes
Pipes can be used to move fluids and items between the blocks as well, Pipez, BBL routers and many more should all work with the filtering system of the blocks as well.

### Troubleshooting
If fluids are not being pushed or pulled make sure the blocks are adjacent and that the output tank of the block pushing the fluid accepts the fluid being pushed.
