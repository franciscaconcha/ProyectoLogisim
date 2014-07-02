package com.cburch.logisim.statediagram.model.exceptions;
/**
 * 
 * @author raticate
 * Excepcion que indica si se intento agregar una transicion con un estado que
 * no esta en el diagrama. En el diagrama de verdad nunca ocurre, se creo para mantener la consisitencia
 * durante el desarrollo y testing.
 */
public class AbsentStateException extends Exception {	}