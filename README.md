# CS4928 Group 35
Leo O'Shea      22342761
Aron Calvert    22370274

## Week 3 Lab Questions

### Point to one if/else you avoided in Order by introducing PaymentStrategy?
Instead of using an if/else statement to executed different code depending on the desired payment method, introducing PaymentStrategy allowed us to simply call the desired strategy's class and execute the code within that class.

### Show (1–2 sentences) how the same Order can be paid by different strategies in the demo without changing Order code?
To do this, we use the PaymentStrategy as an interface that calls the Order method. Then, the classes, ie the different strategies, that implement this interface can override the Order method in order to have the Order method have a different output depending on the strategy/class.

## Week 4 Lab Questions

### How does the Observer pattern improve decoupling in the Café POS system?

The Observer pattern improves decoupling in the Café POS system by allowing different components (observers) to subscribe to updates from the Order class (subject) without the Order class needing to know about the specific implementations of those components. This means that new observers can be added or existing ones modified without changing the Order class.

### Why is it beneficial that new observers can be added without modifying the Order class?

It is beneficial that new observers can be added without modifying the Order class because it adheres to the Open/Closed Principle, which states that software entities should be open for extension but closed for modification. This allows for easier maintenance and scalability of the system.

### Can you think of a real-world system (outside cafés) where Observer is used (e.g., push notifications GUIs)?

A real-world system where the Observer pattern is used is in social media platforms. For example, when a user follows another user, they become an observer of that user's activity. Whenever the followed user posts new content, all their followers (observers) are notified of the update, allowing them to see the new post in their feed without the original user needing to manage or notify each follower individually.

## Week 5 Lab Questions

### Which construction approach you would expose to application developers and why.

Factory based construction using ProductFactory is the appraoch I would expose to application developers. It is simple, hiding the construction logic and decorator chaining, reducing room for error. It standardises the building procedure. It can easily be extended upon, allowing for easy addition of product types.

## Week 8 Lab Questions

### Where does Command decouple UI from business logic in your codebase?
UI changes dont force you to change domain code because it only communicates with PosRemote and Command objects.

### Why is adapting the legacy printer better than changing your domain or vendor class?
Allows for backwards compatability and allows you to test your adapted code with the legacy code.