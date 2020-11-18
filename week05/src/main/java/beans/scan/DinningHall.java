package beans.scan;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DinningHall {
    public void provide() {
        log.info("dinning hall provides breakfast, dinner, supper...");
    }
}
