package beans.scan;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class School {

    @Autowired
    private DinningHall dinningHall;

    public void run() {
        dinningHall.provide();
        log.info("school running smoothly.");
    }
}
