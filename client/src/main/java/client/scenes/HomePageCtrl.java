package client.scenes;

import jakarta.inject.Inject;

public class HomePageCtrl {
    private PrimaryCtrl pc;

    @Inject
    public HomePageCtrl(PrimaryCtrl pc) {
        this.pc = pc;
    }
    // UI interactions of button clicks, text fields (etc.) will go here
}
