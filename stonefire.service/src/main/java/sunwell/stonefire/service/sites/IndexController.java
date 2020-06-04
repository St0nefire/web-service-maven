package sunwell.stonefire.service.sites;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.RedirectView;

import sunwell.stonefire.service.annotation.WebController;

@WebController
public class IndexController
{
    @RequestMapping("/")
    public View index()
    {
    	System.out.println("INDEX IS CALLED");
        return new RedirectView("/welcome", true, false);
    }
}
