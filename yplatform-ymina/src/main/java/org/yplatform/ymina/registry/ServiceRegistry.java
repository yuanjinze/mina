package org.yplatform.ymina.registry;

import java.io.IOException;
import java.util.Set;

import org.yplatform.ymina.io.IoAcceptor;
import org.yplatform.ymina.io.IoHandler;

/**
 * <pre>
 * 这个接口是定义为了内部的Service进行注册,类似于古代的军机处（了解国内和国外的信息）
  <li>{@code Service}
  <li>{@code IoHandler}
  <li>{@code IoAcceptor}
 * </pre>
 * 
 * @author yuanjinze
 *
 */
public interface ServiceRegistry {

    void bind(Service service,IoHandler ioHandler) throws IOException;  
    
    IoAcceptor getIoAcceptor();


}
