package jdirectory.util;

import org.json.simple.JSONObject;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.text.MessageFormat;

/**
 * Represents a utility class which is used to convert
 * input and output application data.
 * 
 * @author Alexander Yurinsky
 */
public class Converter {
    private static final Converter INSTANCE = new Converter();

    /**
     * Gets an instance of the converter.
     *
     * @return An instance of the converter.
     */
    public static Converter getInstance() {
        return INSTANCE;
    }
    private Converter() {}

    /**
     * Populates an instance of request bean with HTTP request parameters
     * that correspond to the bean properties.
     *
     * @param beanClass The class of the request bean.
     * @param request An instance of HTTP servlet request.
     * @param <T> Request bean type.
     * @return An instance of populated request bean.
     * @throws ConversionException If an error encounters while conversion.
     */
    public <T extends Serializable> T populateRequestBean(Class<T> beanClass,
                                           HttpServletRequest request) throws ConversionException {
        try {
            T bean = beanClass.newInstance();
            BeanInfo info = Introspector.getBeanInfo(beanClass);
            for (PropertyDescriptor pd : info.getPropertyDescriptors()) {
                if (pd.getWriteMethod() != null && pd.getPropertyType() == String.class) {
                    String parameter = request.getParameter(pd.getName());
                    if (parameter != null && (parameter = parameter.trim()).length() > 0) {
                        pd.getWriteMethod().invoke(bean, parameter);
                    }
                }
            }
            return bean;
        } catch (Exception e) {
            throw new ConversionException(MessageFormat.format("An error has encountered while " +
                    "populating request bean {0}", beanClass.getName()), e);
        }
    }

    /**
     * Generates JSON response based on properties of the response bean.
     *
     * @param responseBean An instance of response bean.
     * @param responseStream An instance of servlet output stream.
     * @throws ConversionException If an error encounters while generation.
     */
    @SuppressWarnings("unchecked")
    public void generateJSONResponse(Serializable responseBean,
                                            ServletOutputStream responseStream) throws ConversionException {
        JSONObject jsonObj = new JSONObject();
        try {
            BeanInfo info = Introspector.getBeanInfo(responseBean.getClass());
            for (PropertyDescriptor pd : info.getPropertyDescriptors()) {
                if (pd.getReadMethod() != null && !pd.getName().equals("class")) {
                    jsonObj.put(pd.getName(), pd.getReadMethod().invoke(responseBean));
                }
            }
            responseStream.print(jsonObj.toJSONString());
        } catch (Exception e) {
            throw new ConversionException(MessageFormat.format("An error has encountered while " +
                    "generating response for response bean {0}", responseBean.getClass().getName()), e);
        }
    }
}
