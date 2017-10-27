# Lamesauce
A Telegram-Bot for adding messages to a webfrontend (see [HOS](https://github.com/GenuineGuineapig/HOS)) via old school IRC format. 

Lamesauce currently offers these commands:

- `!auth USER` - auths a user to push updates to the webfrontend
- `!deauth USER` - deauths a user, thus stopping him from pushing updates
- `!whoisadmin` - prints the current authorized user list
- `!help` - lists commands and gives a small explanation
- `!bringbeer AMOUNT` - brings beer. As lamesauce is quite efficient as a waiter, he can bring up to 4095 beers at once -- but not more.
- `!add QUOTE` in MoSQL format. Adds to the webfrontend.
- `!areyoualive` - follows a basical [Ping-Pong](https://en.wikipedia.org/wiki/Ping-pong_scheme) principle

### Recommended Usage
For proper usage it is recommended to have `binaries/authedUser.txt` (used for a list of authedUsers) as well as `binaries/add.sh` (Unix-Shell-Script for adding a valid _MoSQL_-Quote \[param: $1\] to _what-ever-you-like-system_ e.g. [HOS](https://github.com/GenuineGuineapig/HOS)) in relative path of `Lamesauce.jar`.

##### Syntax of the _authedUser_-File:
 - \<AuthedUserStorage\> ::= \<authed\>`;`\<username\>`;`\<userFirstName\>
 - \<authed\> = `0` | `1` //=> 0 eq not-authed; 1 eq authed
 
Syntax of _MoSQL_ is available under [HOS](https://github.com/GenuineGuineapig/HOS).

**Thank you for reading**


