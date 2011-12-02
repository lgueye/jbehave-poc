/**
 * 
 */
package fr.explorimmo.poc.domain.exceptions;

/**
 * @author louis.gueye@gmail.com
 */
public class NotFoundException extends BusinessException {

    /**
	 * 
	 */
    private static final long serialVersionUID = 7686464549278707389L;

    /**
     * @param objects
     */
    public NotFoundException(final Object[] messageParams) {
        super("404", messageParams, "404 - Not found");
    }

}
