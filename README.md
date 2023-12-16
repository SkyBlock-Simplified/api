# SimplifiedApi

<a href="https://discord.gg/sbs"><img align="right" src="https://raw.githubusercontent.com/SkyBlock-Simplified/api/master/src/main/resources/sbs_logo_v4.gif?sanitize=true" width="20%"></a>

[![Support Server Invite](https://img.shields.io/discord/652148034448261150?style=for-the-badge&logo=discord&label=SkyBlock%20Simplified)](https://discord.gg/sbs)
[![GitHub Issues](https://img.shields.io/github/issues/SkyBlock-Simplified/api?style=for-the-badge)](https://github.com/SkyBlock-Simplified/api/issues)
[![GitHub Pull Requests](https://img.shields.io/github/issues-pr/SkyBlock-Simplified/api?style=for-the-badge)](https://github.com/SkyBlock-Simplified/api/pulls)
[![GitHub Last Commit](https://img.shields.io/github/last-commit/SkyBlock-Simplified/api?style=for-the-badge)](https://github.com/SkyBlock-Simplified/api/commits/master/)

The SimplifiedApi is a fast, powerful, utility library that provides quick and easy Java development for the
official [Hypixel SkyBlock API](https://api.hypixel.net/).

## ⚡ Quick Example

In this example, we initiate a [default SQL database session](https://github.com/SkyBlock-Simplified/api/blob/master/src/main/java/dev/sbs/api/data/sql/SqlConfig.java#L57),
pull a filtered collection of [ItemModels](https://github.com/SkyBlock-Simplified/api/blob/master/src/main/java/dev/sbs/api/data/model/skyblock/items/ItemModel.java),
and pull the profile of CraftedFury from the [Hypixel SkyBlock API](https://api.hypixel.net/#tag/SkyBlock/paths/~1v2~1skyblock~1profile/get).

```java
public class ExampleApp {
    public static void main(String[] args) {
        // Create a Session
        SimplifiedApi.getSessionManager().connect(SqlConfig.defaultSql());

        // Query Session Repository
        ConcurrentList<ItemModel> campfireTalismans = SimplifiedApi.getRepositoryOf(ItemModel.class)
            .stream()
            .filter(itemModel -> itemModel.getKey().startsWith("CAMPFIRE_TALISMAN"))
            .collect(Concurrent.toList());

        // Retrieve Request Proxy
        HypixelSkyBlockRequest hypixelSkyBlockRequest = SimplifiedApi.getWebApi(HypixelSkyBlockRequest.class);

        // Query Hypixel API
        UUID craftedFuryUUID = "f33f51a7-9691-4076-abda-f66e3d047a71";
        SkyBlockProfilesResponse profiles = hypixelSkyBlockRequest.getProfiles(craftedFuryUUID);

        // Get Selected Island
        SkyBlockIsland island = profiles.getSelected();
        
        // Get Enhanced Island (Database Features, Requires Active Session)
        EnhancedSkyBlockIsland enhancedIsland = island.asEnhanced();
    }
}
```
Make sure you have defined the following environment variables:
```
DATABASE_HOST, DATABASE_SCHEMA, DATABASE_PORT, DATABASE_USER, DATABASE_PASSWORD
```

## 🔗 Quick Links

* [Hypixel API](https://api.hypixel.net/)
* [Hypixel Developers](https://developer.hypixel.net/)
* [Discord API](https://discord.com/developers/docs/intro)
* [Hibernate](https://hibernate.org/)
* [Feign](https://github.com/OpenFeign/feign)
* [Discord](https://discord.gg/sbs)

## <img src="https://cdn.discordapp.com/emojis/929253844284751922.webp" width="25px">&nbsp; Features

* **Reflection** - Helper class and methods to introspect classes, methods and fields. Includes a fast, efficient
class file locator, type-matched methods/constructors, accessor caching, superclass getters, and more.
* **Minecraft**
  * **NbtFactory** - Efficient reading and writing of NBT data structures, independent of the Minecraft client source code.
  * **Ping** - Ping Minecraft servers and get the server information.
  * **Generators** - Generate 1.8.9-identical Chat and Lore, Textured Skulls, Recipes, and more.
* **Math Evaluation** - Evaluate mathematical expressions, includes variable support and many math functions.
* **YAML Config** - Easily build your own YAML file storage by extending a class with `YamlConfig`.
* **Hibernate** - SQL domain model persistence, query the persistence cache via the above quick example, and much more.
* **Feign HTTP Proxies** - Use and build HTTP proxies to quickly access Web API's.
* **Managers**
  * **Service Manager** - Retrieve a built service using the associated class file.
  * **Builder Manager** - Retrieve a builder class using the associated class file.
  * **Key Manager** - Retrieve a key using the associated name.
* **Utilities**
  * **Builder** - Simple helper classes to assist with class/method building using Reflection.
  * **Collections**
    * **Concurrency** - Fast and efficient cross-thread-safe collection classes, including Collection, Deque, List, Map, Queue and Set.
    * **Search** - Quickly search/match Collections/Lists for 1+ objects using method references.
    * **Graph** - Sort a collection that share the same superclass topologically, this is used for efficient Hibernate loading.
  * **Helpers** - Classes to help you with reading and manipulating java types and resources.
  * **Streams**
    * **PairStream** - Custom stream wrapper for Maps that converts `Stream<Map.Entry<K, V>>` to `PairStream<K, V>`.
    AtomicMap and its inheritors all implement this, allowing you to interact using only `K`/`V` on top of `Map.Entry<K, V>`.
    * **TripleStream** - Custom stream wrapper for Collections that converts `Stream<T>` to `TripleStream<T, M, R>`.
    AtomicCollection and its inheritors all implement this as an indexed stream, allowing you to know the position of each element
    and the size of the stream.
  * **Other** - CommandLine, Mutables, Tuples (Pair/Triple), GSON adapters, etc.

## 📦 Standalone Installation

WIP

## <img src="https://cdn.discordapp.com/emojis/929250578499010571.webp" width="25px">&nbsp; Docker Installation

WIP

### Old Instructions

- Docker
  - `docker pull mysql:latest`
  - `docker run -p 3306:3306 --name sbs-mysql -e MYSQL_ROOT_PASSWORD={{ROOT PASSWORD}} -d mysql`
- Container
  - `mysql -uroot -p{{ROOT_PASSWORD}}`
    - `create user 'sbsadmin'@'%' identified by '{{ROOT_PASSWORD}}';`
    - `flush privileges;`
    - `create database skyblocksimplified;`
    - `grant all privileges on skyblocksimplified.* to 'sbsadmin'@'%';`

