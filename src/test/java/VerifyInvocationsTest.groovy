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
        1 * verify.length({ String str -> str.length() > 10} as String)
    }
}
