package client.scenes;

import jakarta.inject.Inject;

public class HomeCtrl {
    private PrimaryCtrl pc;

    @Inject
    public HomeCtrl(PrimaryCtrl pc) {
        this.pc = pc;
    }
    // UI interactions of button clicks, text fields (etc.) will go here
}
