#  Architecture Decision Record (ADR)

# CS4928 Group 35
Leo O'Shea      22342761
Aron Calvert    22370274

## Layers or Partitioning
One decision we had to make was whether to use layers or partitioning to split up the architecture of our project in order to make it more manageable.\
\
Implementing layers would divide our project into four different layers: App, UI, Domain, and Infra. This would make our project more manageable, but keep our project as one large monolith, which is fine for now, but monoliths tend to become too complex and tightly coupled over time as more components are added. This will make it difficult to maintain and update. \
\
As for partitioning the project, this would mean splitting our project in to different services which communicate with the main app through events and REST APIs. This would divide our project in to multiple smaller projects, meaning they would be easier to understand, maintain, and update. Furthermore, these services will not interfere with code within the main app. This easily seemed like the best option until we considered the drawbacks. Creating REST APIs and events for all these individual services would be incredibly time consuming and complex.\
\
We can get away with using layers for now due to our project being on such a small scale, but if we were to continue development on this project, it would be best to spend the time it would take to partition out the services, so to not have one big unmanageable monolith.
