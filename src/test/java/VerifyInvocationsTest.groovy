import spock.lang.Specification

/**
 * Created by mtumilowicz on 2018-07-09.
 */
class VerifyInvocationsTest extends Specification {
    
    def "verify invocations and arguments; all()"() {
        given:
        def verify = Spy(VerifyInvocations)
        
        when:
        verify.all()
        
        then:
        1 * verify.exact("exact")
        
        then:
        1 * verify.length({str -> str.length() > 10})
        
        then:
        1 * verify.pair(_, _) >> {str1, str2 -> str1.length() ==  str2.length()} 
        
        then:
        1 * verify.arguments(*_)
    }
}
