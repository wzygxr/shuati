package class_advanced_algorithms.compression;

import java.util.*;

/**
 * 算术编码实现
 * 
 * 算术编码是一种无损数据压缩方法，它将整个输入消息编码为一个位于[0,1)区间内的实数。
 * 
 * 算法原理：
 * 1. 统计字符频率，构建概率模型
 * 2. 根据概率模型构建累积分布函数(CDF)
 * 3. 对输入字符串进行编码，将整个字符串映射到[0,1)区间的一个子区间
 * 4. 解码时根据相同的概率模型和编码值还原原始字符串
 * 
 * 时间复杂度：
 * - 编码：O(n)，其中n是输入字符串长度
 * - 解码：O(n)，其中n是输出字符串长度
 * 
 * 空间复杂度：O(k)，其中k是不同字符的数量
 * 
 * 优势：
 * 1. 压缩率高，可以达到信息熵的理论极限
 * 2. 可以处理任意精度的概率
 * 3. 适合处理具有明显统计特性的数据
 * 
 * 劣势：
 * 1. 实现复杂，需要处理浮点数精度问题
 * 2. 编码和解码必须使用相同的概率模型
 * 3. 对于短字符串，可能不如其他简单编码方法高效
 * 
 * 应用场景：
 * 1. 图像压缩（JPEG）
 * 2. 音频压缩
 * 3. 数据压缩标准
 */
public class ArithmeticCoding {
    
    // 字符频率映射
    private Map<Character, Integer> frequencyMap;
    // 累积分布函数
    private Map<Character, Long> cumulativeFreq;
    // 总字符数
    private long totalFreq;
    
    /**
     * 构造函数，根据输入字符串构建概率模型
     * @param input 输入字符串
     */
    public ArithmeticCoding(String input) {
        buildFrequencyMap(input);
        buildCumulativeFrequency();
    }
    
    /**
     * 统计字符频率
     * @param input 输入字符串
     */
    private void buildFrequencyMap(String input) {
        frequencyMap = new HashMap<>();
        for (char c : input.toCharArray()) {
            frequencyMap.put(c, frequencyMap.getOrDefault(c, 0) + 1);
        }
        // 添加EOF字符，用于解码时确定结束位置
        frequencyMap.put((char) 0, 1);
    }
    
    /**
     * 构建累积分布函数
     */
    private void buildCumulativeFrequency() {
        cumulativeFreq = new LinkedHashMap<>();
        totalFreq = 0;
        
        // 按字符排序，确保编码和解码使用相同的顺序
        TreeMap<Character, Integer> sortedMap = new TreeMap<>(frequencyMap);
        
        for (Map.Entry<Character, Integer> entry : sortedMap.entrySet()) {
            cumulativeFreq.put(entry.getKey(), totalFreq);
            totalFreq += entry.getValue();
        }
    }
    
    /**
     * 算术编码
     * @param input 输入字符串
     * @return 编码结果（低值和高值）
     */
    public CodeResult encode(String input) {
        // 初始化区间为[0, 1)
        double low = 0.0;
        double high = 1.0;
        
        // 为输入添加EOF字符
        input += (char) 0;
        
        // 对每个字符进行编码
        for (char c : input.toCharArray()) {
            // 计算当前区间的范围
            double range = high - low;
            
            // 获取字符的概率区间
            long symbolLow = cumulativeFreq.get(c);
            long symbolHigh = symbolLow + frequencyMap.get(c);
            
            // 缩小区间
            high = low + range * symbolHigh / totalFreq;
            low = low + range * symbolLow / totalFreq;
        }
        
        return new CodeResult(low, high);
    }
    
    /**
     * 算术解码
     * @param code 编码结果
     * @param maxLength 最大解码长度（防止无限循环）
     * @return 解码结果
     */
    public String decode(CodeResult code, int maxLength) {
        StringBuilder result = new StringBuilder();
        double value = (code.low + code.high) / 2; // 使用区间的中点作为解码值
        
        double low = 0.0;
        double high = 1.0;
        
        while (result.length() < maxLength) {
            double range = high - low;
            
            // 查找对应的字符
            char foundChar = 0;
            boolean found = false;
            
            for (Map.Entry<Character, Long> entry : cumulativeFreq.entrySet()) {
                char c = entry.getKey();
                long symbolLow = entry.getValue();
                long symbolHigh = symbolLow + frequencyMap.get(c);
                
                double symbolLowValue = low + range * symbolLow / totalFreq;
                double symbolHighValue = low + range * symbolHigh / totalFreq;
                
                if (value >= symbolLowValue && value < symbolHighValue) {
                    foundChar = c;
                    low = symbolLowValue;
                    high = symbolHighValue;
                    found = true;
                    break;
                }
            }
            
            if (!found) {
                break;
            }
            
            // 如果是EOF字符，结束解码
            if (foundChar == (char) 0) {
                break;
            }
            
            result.append(foundChar);
        }
        
        return result.toString();
    }
    
    /**
     * 获取字符频率映射（用于调试和分析）
     * @return 字符频率映射
     */
    public Map<Character, Integer> getFrequencyMap() {
        return new HashMap<>(frequencyMap);
    }
    
    /**
     * 编码结果类
     */
    public static class CodeResult {
        public final double low;
        public final double high;
        
        public CodeResult(double low, double high) {
            this.low = low;
            this.high = high;
        }
        
        @Override
        public String toString() {
            return String.format("[%.10f, %.10f)", low, high);
        }
    }
    
    /**
     * 测试方法
     */
    public static void main(String[] args) {
        // 测试用例1：简单字符串
        String test1 = "ABRACADABRA";
        System.out.println("原始字符串: " + test1);
        
        ArithmeticCoding ac = new ArithmeticCoding(test1);
        CodeResult encoded = ac.encode(test1);
        System.out.println("编码结果: " + encoded);
        
        String decoded = ac.decode(encoded, test1.length() + 1);
        System.out.println("解码结果: " + decoded);
        System.out.println("编码解码是否正确: " + test1.equals(decoded));
        System.out.println();
        
        // 测试用例2：包含重复字符的字符串
        String test2 = "AAAAABBBBBCCCCC";
        System.out.println("原始字符串: " + test2);
        
        ArithmeticCoding ac2 = new ArithmeticCoding(test2);
        CodeResult encoded2 = ac2.encode(test2);
        System.out.println("编码结果: " + encoded2);
        
        String decoded2 = ac2.decode(encoded2, test2.length() + 1);
        System.out.println("解码结果: " + decoded2);
        System.out.println("编码解码是否正确: " + test2.equals(decoded2));
        System.out.println();
        
        // 显示字符频率
        System.out.println("字符频率:");
        for (Map.Entry<Character, Integer> entry : ac2.getFrequencyMap().entrySet()) {
            if (entry.getKey() == 0) {
                System.out.println("EOF: " + entry.getValue());
            } else {
                System.out.println(entry.getKey() + ": " + entry.getValue());
            }
        }
    }
}