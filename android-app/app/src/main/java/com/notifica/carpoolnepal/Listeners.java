package com.notifica.carpoolnepal;


public class Listeners {

    public interface CardSelectionListener {
        void onSelect(int position);
    }


    public interface CarpoolSelectionListener {
        void onSelect(Carpool carpool);
    }
}
