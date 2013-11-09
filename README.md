Jedi: Android IPC Mind Tricks
=============================

Winner gets a Mac Pro, solid entries get an interview at Clover. Details here: https://www.clover.com/challenge

At Clover we're working on inter-device coordination and communication. For example, we may have a merchant-facing device that needs to communicate with a customer-facing device to coordinate payment, or with a device in the kitchen to display orders, or to run master-election algorithms to elect master node to do special things like generating sequential order numbers. More general use cases are interesting: causing your friend's phone to view a webpage, dial the phone, share contacts, or start Google Maps navigation.

The Challenge: design and implement a solution to this problem where you extend Android's IPC primitives (Intents, Broadcasts, Binders/Services, Content Providers) to work inter-device.

This challenge is a decathalon: API design, device discovery, network programming, UI design, security (permissioning, authentication, etc.). If you're not particularly strong in one area, that's fine!

We would like the solution to work on a Wifi network (LAN). Creative uses of Bluetooth and NFC might be cool, too.

Judging will be done by the Clover engineering team and will be subjective based on quality of the ideas and execution.

To play: Fork our GitHub repo and submit a pull request when you're ready to submit. Include in your README a high-level description of your approach.
