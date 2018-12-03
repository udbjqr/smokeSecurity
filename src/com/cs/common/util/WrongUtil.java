package com.cs.common.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 用来判断类型转换异常
 */
public class WrongUtil {
  public static final Logger logger = LogManager.getLogger(WrongUtil.class.getName());
  //判断Double类型异常
  public static Double checkAndTakeDoule(Object doule){
    if(doule instanceof String){
      try{
        return Double.parseDouble((String)doule);
      } catch (Exception e){
        logger.error("类型转换异常");
        return null;
      }
    }
    try{
      return (Double) doule;
    }catch (Exception e){
      return null;
    }
  }
  //判断Integer类型异常
  public static Integer checkAndTakestr(Object str){
    if(str instanceof String){
      try{
        return Integer.parseInt((String)str);
      } catch (Exception e){
        logger.error("类型转换异常");
        return null;
      }
    }
    try{
      return (Integer) str;
    }catch (Exception e){
      return null;
    }
  }
}
