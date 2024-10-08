# ViaProxyWebAuthenticator BETA - Make ViaProxyConnect Great Again!
a Web Authenticator Demander for ViaProxy Public instances

# How to host Public Instances
if You are using hosting, you need to just use nip.io or forward their ip to Wildcard DNS Services such as DynuDNS or DuckDNS as the DNS service is for Paid Plan.

1. Start ViaProxy.
2. You Need to use Public Wildcard option. however, Geyser Will not do Ping Passthrough, so you have to turn it off. if you are Geyser User, Turn on forward-hostname on.
3. Now You are hosting an public instance of ViaProxy! the address looks like this: `<Server's Address>_<Server's Port>_<Server's Version>.viaproxy.<yourdomain>:<yourport>`

# List of Public Instances
|address form|Mode|owner|organization|status|
|--|--|--|--|--|
|address_port_version.viaproxy.lenni0451.net:38888 | OpenAuthMod | Lenni0451 & RaphiMC |  ViaVersion | ONLINE |
|address_port_version.viaproxy.raphimc.net:38888 | OpenAuthMod | Lenni0451 & RaphiMC |  ViaVersion | OFFLINE |
|address_port_version.viaproxy.anarchyconnect.duckdns.org:53587 | OpenAuthMod | Me |  GAME-CLI-SRV-DEV | ONLINE |

# usage for address_port_version.viaproxy.anarchyconnect.duckdns.org:53587
* to connect to lenni0451's classic mode test server: the lenni0451's server ip is lenni0451.net:39999 and version is c0.30-CPE.
  * the main address is `lenni0451.net_39999_c0.30-CPE.viaproxy.anarchyconnect.duckdns.org:53587`
* if the address anarchyconnect.duckdns.org don't work anymore you may use `address_port_version.viaproxy.anarchyconnect.ddnsgeek.com:53587`
  * so the sub address is`lenni0451.net_39999_c0.30-CPE.viaproxy.anarchyconnect.ddnsgeek.com:53587`

# WARNING FOR PUBLIC INSTANCES
if the server version has underscore, you have to ommit that part from it.

# Server Version
 * Bedrock: Bedrock-1.21.3
 * Java 1.21.1: 1.21.1-1.7
