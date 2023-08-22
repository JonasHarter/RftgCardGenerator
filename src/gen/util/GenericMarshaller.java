package gen.util;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;


public class GenericMarshaller
{

    public static final Map<String, JAXBContext> CONTEXT_MAP = new HashMap<>();

    public static <T> void marshall(
            T object,
            Path filePath
    )
            throws GenericMarshallerException
    {
        JAXBContext jaxbContext = getContext(
                object.getClass()
        );
        Marshaller jaxbMarshaller;
        try
        {
            jaxbMarshaller = jaxbContext.createMarshaller();

            jaxbMarshaller.setProperty(
                    Marshaller.JAXB_FORMATTED_OUTPUT,
                    true
            );
            OutputStream out = new FileOutputStream(
                    filePath.toString()
            );
            jaxbMarshaller.marshal(
                    object,
                    out
            );
        } catch (
                JAXBException | FileNotFoundException e
        )
        {
            throw new GenericMarshallerException(
                    "Failed to marshall",
                    e
            );
        }
    }

    public static <T> T unmarshall(
            Class<T> clazz,
            Path filePath
    )
            throws GenericMarshallerException
    {

        JAXBContext jaxbContext = getContext(
                clazz
        );
        Unmarshaller jaxbUnmarshaller;
        try
        {
            jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            Object object = jaxbUnmarshaller.unmarshal(
                    filePath.toFile()
            );
            if (
                clazz.isInstance(
                        object
                )
            )
            {
                return clazz.cast(
                        object
                );
            }
        } catch (
            JAXBException e
        )
        {
            throw new GenericMarshallerException(
                    "Failed to unmarshall",
                    e
            );
        }
        throw new GenericMarshallerException(
                "Failed to unmarshall"
        );
    }

    private static JAXBContext getContext(
            Class<?> clazz
    )
            throws GenericMarshallerException
    {
        String className = clazz.getCanonicalName();
        JAXBContext jaxbContext = CONTEXT_MAP.get(
                className
        );
        if (
            jaxbContext == null
        )
        {
            try
            {
                jaxbContext = JAXBContext.newInstance(
                        clazz
                );
            } catch (
                JAXBException e
            )
            {
                throw new GenericMarshallerException(
                        "Failed to create context",
                        e
                );
            }
            CONTEXT_MAP.put(
                    className,
                    jaxbContext
            );
        }
        return jaxbContext;
    }

    public static class GenericMarshallerException
            extends
            Exception
    {

        private static final long serialVersionUID = 6720202823933010851L;

        public GenericMarshallerException()
        {
            super();
        }

        public GenericMarshallerException(
                String message,
                Throwable cause,
                boolean enableSuppression,
                boolean writableStackTrace
        )
        {
            super(
                    message,
                    cause,
                    enableSuppression,
                    writableStackTrace
            );
        }

        public GenericMarshallerException(
                String message,
                Throwable cause
        )
        {
            super(
                    message,
                    cause
            );
        }

        public GenericMarshallerException(
                String message
        )
        {
            super(
                    message
            );
        }

        public GenericMarshallerException(
                Throwable cause
        )
        {
            super(
                    cause
            );
        }

    }

}
