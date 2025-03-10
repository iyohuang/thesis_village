package com.thesis.village.aop;

import com.thesis.village.dao.UserMapper;
import com.thesis.village.dao.UserPermissionMapper;
import com.thesis.village.enums.HttpStatusEnum;
import com.thesis.village.model.BusinessException;
import com.thesis.village.model.auth.User;
import com.thesis.village.utils.ThreadLocalUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author yh
 */

@Component
@Aspect
@Slf4j
public class PermissionAspect {

    private final static String EXPRESSION = "@annotation(com.thesis.village.aop.RequiresPermission)";

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserPermissionMapper userPermissionMapper;
    // 切入点
    @Pointcut("@annotation(com.thesis.village.aop.RequiresPermission)")
    public void checkPermission() {}
    
    @Around("checkPermission() && @annotation(requiresPermission)")
    public Object checkPermission(ProceedingJoinPoint joinPoint, RequiresPermission requiresPermission) throws Throwable {
    
        log.info("checkPermission: " + requiresPermission);
        // 获取注解中的操作类型
        String operationType = requiresPermission.operationType();
        Map<String, Object> map = ThreadLocalUtil.get();
        Long id = ((Number) map.get("id")).longValue();
        User currentUser = userMapper.findById(id);
        String roleType = currentUser.getRoleType();
        if(operationType.equals("myself")){
            if(roleType.equals("superadmin")) return joinPoint.proceed();
            if((roleType.equals("user") || roleType.equals("admin") ) && currentUser.getId().equals(id)){
                if(userPermissionMapper.selectCountByUserIdAndPermissionCode(currentUser.getId(),requiresPermission.value())>0){
                    throw new BusinessException(HttpStatusEnum.NO_PERMISSION);
                }
                return joinPoint.proceed();
            }
        }else{
            if(roleType.equals("superadmin")) return joinPoint.proceed();
            if(roleType.equals("admin")) {
                if(userPermissionMapper.selectCountByUserIdAndPermissionCode(currentUser.getId(),requiresPermission.value())>0){
                    return joinPoint.proceed();
                }
                throw new BusinessException(HttpStatusEnum.NO_PERMISSION);
            }
            if(roleType.equals("user")) {
                throw new BusinessException(HttpStatusEnum.NO_PERMISSION);
            }
        }
        throw new BusinessException(HttpStatusEnum.NO_PERMISSION);
    }
    
    
}
