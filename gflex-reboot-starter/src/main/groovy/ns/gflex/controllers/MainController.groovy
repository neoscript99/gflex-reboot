package ns.gflex.controllers

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * Created by Neo on 2017-06-27.
 */

@RestController
@RequestMapping("/info")
class MainController {

    @RequestMapping("")
    String info() {
        return 'Welcome to gflex reboot!'
    }
}
