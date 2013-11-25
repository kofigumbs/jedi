What is Shamrock?
=========

Solution to the Clover Jedi challenge
---
Main goal of the app is to create an app that allows inter-device coordination and communication. See the rest of the problem description for more details (http://www.github.com/clover/jedi). Winner gets a Mac Pro, but *more importantly* an on-site job interview (wink wink)!


Overview
---
Shamrock uses a NFC and TCP for a one-two punch knockout. First, users pair the devices by opening the app on each, touching the phones' NFC transmitters together, then tapping the screen of one device. That's it! The phones are connected! The device whose screen was tapped is the server, and the other device is the client. The client sends commands via the on-screen text field, and the server responds automatically by opening the relevant app to display the information. 

The command transfer uses TCP, so the devices must be connected to the same wifi network, but that network need not be online. Both phones are free to navigate away from the app, and the necessary service will run in the background (though I recommend that the server remains charging). Shamrock is currently only configured to take phone numbers, urls, and map queries.

Screenshots: http://goo.gl/dUQEpJ


Motivation
---
**NFC** - I chose NFC because it was the simplest and most user-secure way to transfer the server's IP address. Using NFC allows me to automate all of the connnection process under the hood for both the client and the server. This means theusernever has to do any manual authentication: just tap and start commanding. NFC also some provides security because it means that users will be very aware of who has access to their phone. Of course, this is not fool-proof since unatended phones are always vulnerable. Also I'm not sure how secure NFC is as a whole, so the security of the app is based on that.

**TCP** - Honestly, this decisio was almost strictly because WifiDirect sucked. I tried to implement the solution using Android's built in classes, but even the sample code didn't work. So dealing with sockets was the "manual attempt," but it actually works quite simply. Server listens for commands from client in the form of strings that contain the type and the data. Server then reacts accordingly by opening the app necessary to view data even if Shamrock is not in foreground.


Known Issues
---
- Server doesn't notify client when it disconnects
- Client notifies server, but server only checks on activity resume
- Should give users option to enable wifi if it is not detected (instead of finshing)
- Button selectors
- Only tested on two devices, so probably a bunch of others
- Lazy error trapping/alerting system


Citations
---
I got the NFC diagram image from here: http://goo.gl/7zUbp1

Everything else was my original work or based upon open-source projects.

Oh, except for the Cover logo.
