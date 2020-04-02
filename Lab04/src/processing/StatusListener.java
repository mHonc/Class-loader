package processing;

public interface StatusListener {
    /**
     * Metoda dostarczajÄca sĹuchaczowi status przetwarzania zadania
     * s - status przetwarzania zadania
     */
    void statusChanged(Status s);
}