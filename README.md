# spock-verify-arguments
The main goal of this project is to give simple examples of how to verify 
methods invocations and their arguments.

_Reference_: [Integration based testing](http://spockframework.org/spock/docs/1.0/interaction_based_testing.html)  
_Reference_: [Declaring interactions](http://spockframework.org/spock/docs/1.1-rc-3/all_in_one.html#_where_to_declare_interactions)

# preface
Like Mockito Spock is lenient by default. This means that unexpected 
method calls on mock objects (or, in other words, interactions that 
aren’t relevant for the test at hand) are allowed and answered with a 
default response.

Spock’s mocking framework makes it easy to describe only what’s 
relevant about an interaction, avoiding the over-specification trap.

# manual
* verifying invocations

    if you have a method `all()` in class `XXX`:
    ```
    all() {
        ...
        method1(arg1_1, arg1_2, ...)
        method2(arg2_1, arg2_2, ...)
        .
        .
        .
    }    
    ```
    and you want to verify if when you call `all()` then `method1, 
    method2, ...` will be called exactly once:
    ```
    given:
    def verify = Spy(XXX)
    
    when:
    verify.all()
    
    then:
    1 * verify.method1(*_)
    
    then:
    1 * verify.method2(*_)  
    ```
    
    _Remark_: `*_` any number of any arguments.  
    _Remark_: Invocations will be checked in the same order as `then:`
    parts. If we have only one `then:` section the order does not matter.
    
* verifying arguments

    if you have a method `all()` in class `XXX`:
    ```
    all() {
        ...
        method1(arg1_1)
        method2(arg2_1, arg2_2)
        method3("exact")
    }    
    ```
    and you want to verify if when you call `all()` then parameters passed 
    to `method1, method2` have certain state:
    ```
    given:
    def verify = Spy(XXX)
    
    when:
    verify.all()
    
    then:
    1 * verify.method1({str -> str.length() > 10})
    1 * verify.pair(_,_) >> {str1, str2 -> str1.length() ==  str2.length()} // correlated states of arguments
    1 * verify.method3("exact") // exact matching
    ```    
    _Remark_: Order in `then:` part is insignificant.

* declaring interactions
    * introduction  
        So far, we declared all our interactions in a `then:` block. When an 
        invocation on a mock object occurs, it is matched against interactions 
        in the interactions' declared order. If an invocation matches multiple 
        interactions, the earliest declared interaction that hasn’t reached 
        its upper invocation limit will win. There is one exception to this 
        rule: **Interactions declared in a `then:` block are matched against before 
        any other interactions.**
    
    * how are interactions recognized?
        If an expression is in statement position and is either a 
        multiplication `(*)` or a right-shift `(>>, >>>)` operation, 
        then it is considered an interaction and will be parsed 
        accordingly.
        
    * combining mocking and stubbing
        When mocking and stubbing the same method call, they have to 
        happen in the same interaction. In particular, the following 
        Mockito-style splitting of stubbing and mocking into two separate 
        statements **will not work**:
        ```
        given:
        subscriber.receive("message1") >> "ok"
        
        when:
        publisher.send("message1")
        
        then:
        1 * subscriber.receive("message1")
        ```
        because the receive call will first get matched against the 
        interaction in the `then:` block. Since that interaction doesn’t 
        specify a response, the default value for the method’s return 
        type (null in this case) will be returned - this is just another 
        facet of Spock’s lenient approach to mocking. 
        Hence, the interaction in the `setup:` block will never get a 
        chance to match.
        
        **Mocking and stubbing of the same method call has to happen in the 
        same interaction.**
        ```
        when:
        publisher.send("message1")
        
        then:
        1 * subscriber.receive("message1") >> "ok"
        ```
        
