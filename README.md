# Starts With A [v1.0.0]
SWA, aka "Starts With A", is an Android application for people who often pose the following type of question:

_Hey, what's that word that starts with a 'q' and is kinda like a round hard clam?_

Although any normal person would immediately answer "quahaug", the asker might not be blessed with such normal friends or colleagues. And in that case, he or she would be able to turn to SWA for easy, streamlined aid. SWA will always be there for you! Except when your phone battery dies.

## Table of Contents
* [Background](#background)
* [Features](#features)
* [Implementation](#implementation)
* [Images](#images)
* [Download](#download)
* [FAQ](#faq)
* [License](#license)
* [Credits](#credits)

## Important Announcement
This app only supports **nouns**. Yes, I will say this approximately twenty more times just to make sure you notice.

## Background
Born of a million "what's that word" quotes by my friends and a desire to put some of the concepts from Berkeley's [CS61B Project 1](#credits) to use, SWA is the first Android application ever developed by me. That being the case, I really hope it's usable!

## Features
Are you struggling to think of a word that you _know_ is on the tip of your tongue? Do you need another way to say "nitrocellulose"? Have you momentarily forgotten how to speak English? Enter a starting letter and any sentence that comes to mind alongside the vague imprint of this word in your memory. SWA will match your input with a noun,<sup>*</sup> which is hopefully the noun you were looking for.

This app also features a decorative geometric background. :)

<sup>*</sup> At the moment – and for the foreseeable future – SWA only includes support for English nouns.

## Implementation
SWA utilizes a number of internal hash maps to keep track of word synsets and dictionary definitions. It also relates hyponyms via a directed graph (using a very barebones Digraph class implemented by yours truly). These structures are stored as part of a data interface object and are populated upon startup (while the user is being shown a splash screen). The scoring algorithm in the main activity can then handle this data via the object's public access methods.

More about that scoring algorithm. Like the similarly named Stable Marriage Algorithm (SMA), this algorithm must pair the user's input with a single "best-match" word. For every member of the association input, we will filter for words starting with the user-specified letter and then consider each word in the set of synonyms and hyponyms. (One of these will eventually be chosen.) 

The algorithm will award 1 point to a word for every time a user-selected word appears in the association's definition. It will also award 2 points to the association just for showing up in the first place. At the end of it all, the algorithm will choose the associated word that accumulated the most points overall.

## Images
![alt text](https://cloud.githubusercontent.com/assets/8358648/9465784/755298d4-4ae4-11e5-88d1-68504e82f889.png "SWA demo images")

## Download
Because the app only supports nouns (and I'm a poor college student with a slight aversion to $25 registration fees), I have decided to hold off on uploading SWA to the Google Play Store. Instead, I have assembled an unsigned APK file, which can be used to install the app via a slightly sketchier method called sideloading. This is how you do that:

1. Download [the APK file](https://github.com/ohjay/SWA/blob/master/apk/app-release-unsigned.apk).
2. Follow the instructions [here](http://www.cnet.com/how-to/how-to-install-apps-outside-of-google-play/). (Hey, why reinvent the wheel?)
3. If there's a problem, feel free to contact me at the email in my profile sidebar!

## FAQ
- _Isn't this just an overglorified thesaurus?_

  That's one way of looking at it. However, we at Owen Jow Inc.<sup>*</sup> believe our app has a slight niche, that being its speed and optimization in identifying words starting with a particular letter and having similarities to (or definitions involving) certain other nouns. [Note: I say "nouns" because this app only provides support for nouns.]
- _Why is SWA only able to suggest nouns?_

  First of all, SWA would take about four times as long to load (and would be about four times as big!) if I had included other types of words. 
  
  I know – that's more of a hindsight justification than a real answer. In truth, it comes down to two things: the fact that my current WordNet datasets only include noun associations, and my belief that nouns are the main selling point of SWA's concept anyway. Yes, I could spend some time learning how to use a WordNet API to parse verbs/adjectives into the required CSV .txt format. But when I consider the implications of a complete dictionary inclusion – that the user'll be staring at a loading screen for 40 seconds and will probably be on the hunt for a noun anyway – it just doesn't seem worth it. Nouns it is, and nouns it remains.
- _Why do you have to spend so much time loading in the first place?_

  Well, I have to process all the WordNet data and add it to my internal data structures. Considering the number of words and synsets that need to be parsed – there's about 9 MB of plain text to go through – a bit of loading time shouldn't come as too much of a surprise.
- _Did you have to use stock fonts?_

  Yes, because otherwise I would have needed to import external fonts as assets. This would have had a slight impact on memory and performance.
- _This app sucks!_

  Joke's on you; that's not a question.
- _In "Starts With A", is the pronunciation emphasis on the 'A' or the "Starts"?_

  It should be read **Starts** with A. When I say "Starts With A", I'm not suggesting that a word starts with the letter 'A'; rather, I'm allowing the starting letter to remain an ambiguous entity (because it can be whatever you want). 
  
  Of course, if it makes you feel better or I just ruined the entire app for you, you can view the title itself as an ambiguous entity. In other words, you can ignore everything I just said and imagine that the app title refers to a word that starts with 'A'. Perhaps because 'A' is the first letter of the alphabet?
- _Are you just making all these questions up yourself? I mean, you have no friends._

  :'( single tear...

<sup>*</sup> Not a real thing. By the way, this app only supports nouns.

## License
SWA is distributed under the terms of the [MIT license](https://github.com/ohjay/SWA/blob/master/LICENSE).

## Credits
- Owen Jow - Development, (basic) Graphic Design
- WordNet<br>Princeton University "About WordNet." WordNet. Princeton University. 2010. <http://wordnet.princeton.edu>
- [CS61B Project 1 [for inspiration]](http://berkeley-cs61b.github.io/public_html/materials/proj/proj1/proj1.html)
