# Roll for the Galaxy Card Generator

A card generator for the Boardgame Roll for the Galaxy<br />
The intent was to replace the tiles in the game with cards.<br />
Currently german is the only available language.<br />
It includes the base game, rivalry, ambition and some promo cards.<br />

All images are AI generated with MidJourney.<br />
This uses Inkscape and flowRoot (from svg 1.2), for conversion to png.<br />
This uses the DM Serif font. It needs to be installed, so Inkscape can use it.<br />

# Explanation

<img src="/readMe/1.png" width="200" />
<img src="/readMe/2.png" width="200" />
<img src="/readMe/3.png" width="200" />
<img src="/readMe/4.png" width="200" />
<img src="/readMe/5.png" width="200" />
<img src="/readMe/6.png" width="200" />
<img src="/readMe/7.png" width="200" />

The double-sized starting tiles are split, but marked with a black square that contains a number at the top corner.
Pairs with identical numbers form a starting tile. 
The starting worlds are marked like this as well, but without a number.<br />

All cards have a unique id at the bottom, ending in A or B. These ids allow to match corresponding pairs.<br />
The phase icons are bigger and on the right of the image. There is an additional icon for cards that effect the assignment phase.<br />
There are no icons for the specific card effects.<br />
The icon ⬡ stands for victory points.<br />
The icon ⬢ denotes victory points, that will be gained at the end of the game.<br />
The icons ⌈⌉ and ⌊⌋ denote rounding up or down.<br />

# Ideas

- Support for locale specific text and images
- Automatically check for unused images
- Bigger text to fill space
- Center header vertically
- Backside for single sided cards
- Starting planets no A/B in id
- Improve centering & shape of cost and plus
- Change bg color back to #bfbfbf for proper printing