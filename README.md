# spock-verify-arguments
The main goal of this project is to give simple examples of how to verify 
methods invocations and their arguments.

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