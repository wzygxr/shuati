package class133;

/**
 * =================================================================================
 * 高斯消元法异常处理类 - GaussException.java
 * =================================================================================
 * 
 * 功能描述：
 * 提供高斯消元法相关的异常类型和错误处理机制，增强代码的健壮性和可维护性
 * 
 * 异常分类：
 * 1. 输入验证异常：参数范围、格式错误等
 * 2. 数值计算异常：数值溢出、精度问题等
 * 3. 算法逻辑异常：矩阵奇异、无解等情况
 * 4. 系统资源异常：内存不足、IO错误等
 * 
 * 设计原则：
 * 1. 异常层次清晰：继承关系合理，便于捕获和处理
 * 2. 信息丰富：提供详细的错误信息和上下文
 * 3. 可恢复性：区分可恢复和不可恢复异常
 * 4. 国际化支持：预留多语言错误消息支持
 * 
 * 作者：算法之旅项目组
 * 版本：v1.0
 * 日期：2025-10-28
 * =================================================================================
 */

/**
 * 高斯消元法基础异常类
 */
class GaussException extends Exception {
    private final ErrorCode errorCode;
    private final String detailedMessage;
    
    public GaussException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
        this.detailedMessage = message;
    }
    
    public GaussException(ErrorCode errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.detailedMessage = message;
    }
    
    public ErrorCode getErrorCode() {
        return errorCode;
    }
    
    public String getDetailedMessage() {
        return detailedMessage;
    }
    
    @Override
    public String toString() {
        return String.format("GaussException[code=%s, message=%s]", 
                           errorCode, getMessage());
    }
}

/**
 * 输入验证异常
 */
class GaussInputException extends GaussException {
    public GaussInputException(String message) {
        super(ErrorCode.INPUT_VALIDATION, message);
    }
    
    public GaussInputException(String message, Throwable cause) {
        super(ErrorCode.INPUT_VALIDATION, message, cause);
    }
}

/**
 * 数值计算异常
 */
class GaussNumericalException extends GaussException {
    public GaussNumericalException(String message) {
        super(ErrorCode.NUMERICAL_ERROR, message);
    }
    
    public GaussNumericalException(String message, Throwable cause) {
        super(ErrorCode.NUMERICAL_ERROR, message, cause);
    }
}

/**
 * 算法逻辑异常
 */
class GaussAlgorithmException extends GaussException {
    public GaussAlgorithmException(String message) {
        super(ErrorCode.ALGORITHM_ERROR, message);
    }
    
    public GaussAlgorithmException(String message, Throwable cause) {
        super(ErrorCode.ALGORITHM_ERROR, message, cause);
    }
}

/**
 * 系统资源异常
 */
class GaussSystemException extends GaussException {
    public GaussSystemException(String message) {
        super(ErrorCode.SYSTEM_ERROR, message);
    }
    
    public GaussSystemException(String message, Throwable cause) {
        super(ErrorCode.SYSTEM_ERROR, message, cause);
    }
}

/**
 * 错误代码枚举
 */
enum ErrorCode {
    // 输入验证错误
    INPUT_VALIDATION("E001", "输入验证失败"),
    MATRIX_SIZE_INVALID("E002", "矩阵尺寸无效"),
    PARAMETER_OUT_OF_RANGE("E003", "参数超出范围"),
    
    // 数值计算错误
    NUMERICAL_ERROR("E101", "数值计算错误"),
    PRECISION_LOSS("E102", "精度损失严重"),
    OVERFLOW_DETECTED("E103", "数值溢出检测"),
    UNDERFLOW_DETECTED("E104", "数值下溢检测"),
    
    // 算法逻辑错误
    ALGORITHM_ERROR("E201", "算法逻辑错误"),
    SINGULAR_MATRIX("E202", "矩阵奇异，无唯一解"),
    NO_SOLUTION("E203", "方程组无解"),
    INFINITE_SOLUTIONS("E204", "方程组有无穷多解"),
    
    // 系统资源错误
    SYSTEM_ERROR("E301", "系统资源错误"),
    MEMORY_ALLOCATION_FAILED("E302", "内存分配失败"),
    IO_OPERATION_FAILED("E303", "IO操作失败"),
    TIMEOUT_EXCEEDED("E304", "计算超时");
    
    private final String code;
    private final String description;
    
    ErrorCode(String code, String description) {
        this.code = code;
        this.description = description;
    }
    
    public String getCode() {
        return code;
    }
    
    public String getDescription() {
        return description;
    }
}

/**
 * 输入验证工具类
 */
class GaussValidator {
    
    /**
     * 验证矩阵尺寸
     */
    public static void validateMatrixSize(int n, int maxSize) throws GaussInputException {
        if (n <= 0) {
            throw new GaussInputException(
                String.format("矩阵尺寸必须为正数，实际值：%d", n),
                ErrorCode.MATRIX_SIZE_INVALID
            );
        }
        
        if (n > maxSize) {
            throw new GaussInputException(
                String.format("矩阵尺寸超出限制，最大值：%d，实际值：%d", maxSize, n),
                ErrorCode.PARAMETER_OUT_OF_RANGE
            );
        }
    }
    
    /**
     * 验证矩阵数据
     */
    public static void validateMatrixData(double[][] matrix, int n) throws GaussInputException {
        if (matrix == null) {
            throw new GaussInputException("矩阵不能为null", ErrorCode.INPUT_VALIDATION);
        }
        
        if (matrix.length < n) {
            throw new GaussInputException(
                String.format("矩阵行数不足，期望：%d，实际：%d", n, matrix.length),
                ErrorCode.MATRIX_SIZE_INVALID
            );
        }
        
        for (int i = 0; i < n; i++) {
            if (matrix[i] == null || matrix[i].length < n + 1) {
                throw new GaussInputException(
                    String.format("矩阵第%d行列数不足", i + 1),
                    ErrorCode.MATRIX_SIZE_INVALID
                );
            }
        }
    }
    
    /**
     * 验证数值范围
     */
    public static void validateNumericalRange(double value, String context) throws GaussNumericalException {
        if (Double.isNaN(value)) {
            throw new GaussNumericalException(
                String.format("%s包含NaN值", context),
                ErrorCode.NUMERICAL_ERROR
            );
        }
        
        if (Double.isInfinite(value)) {
            throw new GaussNumericalException(
                String.format("%s包含无穷大值", context),
                ErrorCode.OVERFLOW_DETECTED
            );
        }
        
        // 检查是否接近数值极限
        double absValue = Math.abs(value);
        if (absValue > 1e100) {
            throw new GaussNumericalException(
                String.format("%s数值过大：%.2e", context, value),
                ErrorCode.OVERFLOW_DETECTED
            );
        }
        
        if (absValue < 1e-100 && absValue > 0) {
            throw new GaussNumericalException(
                String.format("%s数值过小：%.2e", context, value),
                ErrorCode.UNDERFLOW_DETECTED
            );
        }
    }
}

/**
 * 异常处理工具类
 */
class GaussExceptionHandler {
    
    /**
     * 安全执行高斯消元计算
     */
    public static SolutionResult safeGaussComputation(GaussComputation computation) {
        try {
            return computation.execute();
        } catch (GaussInputException e) {
            // 输入错误，用户可修复
            System.err.println("输入错误：" + e.getDetailedMessage());
            return SolutionResult.error(e.getErrorCode(), "请检查输入数据");
        } catch (GaussNumericalException e) {
            // 数值计算错误，可能需要调整参数
            System.err.println("数值计算错误：" + e.getDetailedMessage());
            return SolutionResult.error(e.getErrorCode(), "数值稳定性问题，建议调整精度参数");
        } catch (GaussAlgorithmException e) {
            // 算法逻辑错误，需要修复代码
            System.err.println("算法逻辑错误：" + e.getDetailedMessage());
            return SolutionResult.error(e.getErrorCode(), "算法实现问题，请联系开发人员");
        } catch (GaussSystemException e) {
            // 系统资源错误，需要系统干预
            System.err.println("系统资源错误：" + e.getDetailedMessage());
            return SolutionResult.error(e.getErrorCode(), "系统资源不足，请释放内存后重试");
        } catch (Exception e) {
            // 未知错误
            System.err.println("未知错误：" + e.getMessage());
            return SolutionResult.error(ErrorCode.SYSTEM_ERROR, "未知错误发生");
        }
    }
    
    /**
     * 记录异常日志
     */
    public static void logException(GaussException exception) {
        // 在实际应用中，这里应该使用日志框架如Log4j、SLF4J等
        System.err.println("[" + exception.getErrorCode() + "] " + exception.getDetailedMessage());
        if (exception.getCause() != null) {
            exception.getCause().printStackTrace();
        }
    }
}

/**
 * 高斯计算接口
 */
interface GaussComputation {
    SolutionResult execute() throws GaussException;
}

/**
 * 解的结果封装
 */
class SolutionResult {
    private final boolean success;
    private final ErrorCode errorCode;
    private final String message;
    private final double[] solution;
    
    private SolutionResult(boolean success, ErrorCode errorCode, String message, double[] solution) {
        this.success = success;
        this.errorCode = errorCode;
        this.message = message;
        this.solution = solution;
    }
    
    public static SolutionResult success(double[] solution) {
        return new SolutionResult(true, null, "计算成功", solution);
    }
    
    public static SolutionResult error(ErrorCode errorCode, String message) {
        return new SolutionResult(false, errorCode, message, null);
    }
    
    public boolean isSuccess() { return success; }
    public ErrorCode getErrorCode() { return errorCode; }
    public String getMessage() { return message; }
    public double[] getSolution() { return solution; }
}

/**
 * =================================================================================
 * 使用示例
 * =================================================================================
 * 
 * 示例1：基本异常处理
 * ```java
 * try {
 *     GaussValidator.validateMatrixSize(n, MAX_SIZE);
 *     GaussValidator.validateMatrixData(mat, n);
 *     int result = gauss(mat, n);
 * } catch (GaussInputException e) {
 *     // 处理输入错误
 *     System.err.println("输入错误：" + e.getMessage());
 * } catch (GaussNumericalException e) {
 *     // 处理数值错误
 *     System.err.println("数值错误：" + e.getMessage());
 * }
 * ```
 * 
 * 示例2：安全计算模式
 * ```java
 * SolutionResult result = GaussExceptionHandler.safeGaussComputation(() -> {
 *     // 执行高斯消元计算
 *     GaussValidator.validateMatrixSize(n, MAX_SIZE);
 *     int status = gauss(mat, n);
 *     if (status == 0) {
 *         throw new GaussAlgorithmException("矩阵奇异，无唯一解");
 *     }
 *     return extractSolution(mat, n);
 * });
 * 
 * if (result.isSuccess()) {
 *     // 处理成功结果
 *     double[] solution = result.getSolution();
 * } else {
 *     // 处理错误结果
 *     System.err.println("计算失败：" + result.getMessage());
 * }
 * ```
 * 
 * 设计模式应用：
 * 1. 策略模式：不同的异常处理策略
 * 2. 工厂模式：异常对象创建
 * 3. 模板方法：安全计算流程
 * 4. 装饰器模式：异常处理包装
 * =================================================================================
 */