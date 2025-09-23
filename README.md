# CS4928 Group 35
Leo O'Shea      22342761
Aron Calvert    22370274

## Week 3 Lab Questions

### Point to one if/else you avoided in Order by introducing PaymentStrategy?
Instead of using an if/else statement to executed different code depending on the desired payment method, introducing PaymentStrategy allowed us to simply call the desired strategy's class and execute the code within that class.

### Show (1–2 sentences) how the same Order can be paid by different strategies in the demo without changing Order code?
To do this, we use the PaymentStrategy as an interface that calls the Order method. Then, the classes, ie the different strategies, that implement this interface can override the Order method in order to have the Order method have a different output depending on the strategy/class.
