# CS4928 Group 35
Leo O'Shea      22342761\
Aron Calvert    22370274

**Note: Removed contents before week 8, as the final is only assessing weeks 8 onwards. Previous questions can be found on older merges on our [GitHub](https://github.com/VenturiVen/CS4928_Labs)**

## Week 8 Lab Questions

### Where does Command decouple UI from business logic in your codebase?
UI changes dont force you to change domain code because it only communicates with PosRemote and Command objects.

### Why is adapting the legacy printer better than changing your domain or vendor class?
Allows for backwards compatability and allows you to test your adapted code with the legacy code.

## Week 9 Lab Questions

### Where did you choose **safety** over **transparency** in your Composite API and why?
I chose safety over transparency in the MenuComponent.java. By default, its operations throw an exception, which is a safe design choice because it prevents misuse (for example, trying to add a child to a leaf). This balances transparency and safety: the API is visible, but only supported in the right subclasses.

###  What new behavior becomes easy with State that was awkward with conditionals?
With State, the order will know what to do when it changes state instead of me having to write long if/else statements that checks conditionals.

## Week 10 Lab Questions

### Why did you choose a Layered Monolith (for now) rather than partitioning into multiple services?
I chose a layered monolith as partitioning this project into multiple service would require extra complexity than necessary at the moment. This includes implementing features such as networking and APIs to allow communication between the multiple services. Furthermore, the layers currently already provide enough structure.

### Which seams are natural candidates for future partitioning (e.g., Payments, Notifications)?
Seams such as Payments, Notifications, and Menu could be partitioned in the future. This is because they all have enough purpose to be their own service and they are seperate enough from the main app already that they could be moved to their own service without affecting the other components too much.

### What are the connectors/protocols you would define if splitting (events, REST APIs)? Keep the system simple today, but be deliberate about future evolution.
If splitting up the services in the future, I would use events and REST APIs in order for the services to communicate with the main app.
For the REST API, I would have calls such as:
- Payments: POST /pay
- Notifications: POST /send
- Menu: GET /menu, GET /item, POST /item \
\
As for events, I alredy have events classes defined that manage orders. I would keep those events and add more such as:
- OrderCreated
- OrderPaid
- OrderCancelled
- OrderReady