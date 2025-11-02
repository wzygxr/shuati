package class_advanced_algorithms.compression;

import java.util.*;

/**
 * Huffman编码实现
 * 
 * Huffman编码是一种无损数据压缩算法，它根据字符出现的频率为每个字符分配不同长度的编码，
 * 频率高的字符分配较短的编码，频率低的字符分配较长的编码，从而实现数据压缩。
 * 
 * 算法原理：
 * 1. 统计输入数据中每个字符的频率
 * 2. 构建Huffman树（最优二叉树）
 * 3. 根据Huffman树生成每个字符的编码
 * 4. 使用生成的编码对原始数据进行编码
 * 5. 解码时根据Huffman树和编码还原原始数据
 * 
 * 时间复杂度：
 * - 构建Huffman树：O(n log n)，其中n是不同字符的数量
 * - 编码：O(m)，其中m是输入数据的长度
 * - 解码：O(m)，其中m是编码后数据的长度
 * 
 * 空间复杂度：O(n)，其中n是不同字符的数量
 * 
 * 优势：
 * 1. 压缩率高，特别是对于字符频率差异较大的数据
 * 2. 实现相对简单
 * 3. 解码过程确定且无歧义
 * 4. 前缀编码特性保证了解码的唯一性
 * 
 * 劣势：
 * 1. 需要传输或存储Huffman树信息
 * 2. 对于字符频率分布均匀的数据压缩效果不佳
 * 3. 需要两次遍历数据（统计频率和编码）
 * 
 * 应用场景：
 * 1. 文件压缩（如ZIP格式）
 * 2. 图像压缩（JPEG中的部分应用）
 * 3. 网络传输数据压缩
 */
public class HuffmanEncoding {
    
    // Huffman树节点
    static class Node implements Comparable<Node> {
        char character;      // 字符（仅叶节点有值）
        int frequency;       // 频率
        Node left, right;    // 左右子树
        
        // 构造函数（叶节点）
        Node(char character, int frequency) {
            this.character = character;
            this.frequency = frequency;
        }
        
        // 构造函数（内部节点）
        Node(int frequency, Node left, Node right) {
            this.frequency = frequency;
            this.left = left;
            this.right = right;
        }
        
        // 判断是否为叶节点
        boolean isLeaf() {
            return left == null && right == null;
        }
        
        // 比较方法，用于优先队列
        @Override
        public int compareTo(Node other) {
            return Integer.compare(this.frequency, other.frequency);
        }
    }
    
    // 编码结果类
    public static class EncodeResult {
        public final String encodedData;     // 编码后的数据
        public final Map<Character, String> huffmanCodes;  // Huffman编码表
        
        public EncodeResult(String encodedData, Map<Character, String> huffmanCodes) {
            this.encodedData = encodedData;
            this.huffmanCodes = huffmanCodes;
        }
    }
    
    /**
     * 构建Huffman树
     * @param frequencyMap 字符频率映射
     * @return Huffman树的根节点
     */
    private static Node buildHuffmanTree(Map<Character, Integer> frequencyMap) {
        // 创建优先队列（最小堆）
        PriorityQueue<Node> pq = new PriorityQueue<>();
        
        // 将所有字符节点加入优先队列
        for (Map.Entry<Character, Integer> entry : frequencyMap.entrySet()) {
            pq.offer(new Node(entry.getKey(), entry.getValue()));
        }
        
        // 特殊情况：只有一个字符
        if (pq.size() == 1) {
            Node node = pq.poll();
            return new Node(node.frequency, node, null);
        }
        
        // 构建Huffman树
        while (pq.size() > 1) {
            // 取出频率最小的两个节点
            Node left = pq.poll();
            Node right = pq.poll();
            
            // 创建新的内部节点
            Node parent = new Node(left.frequency + right.frequency, left, right);
            
            // 将新节点加入优先队列
            pq.offer(parent);
        }
        
        // 返回根节点
        return pq.poll();
    }
    
    /**
     * 生成Huffman编码表
     * @param root Huffman树根节点
     * @return 字符到编码的映射
     */
    private static Map<Character, String> generateHuffmanCodes(Node root) {
        Map<Character, String> huffmanCodes = new HashMap<>();
        generateCodes(root, "", huffmanCodes);
        return huffmanCodes;
    }
    
    /**
     * 递归生成编码
     * @param node 当前节点
     * @param code 当前路径的编码
     * @param huffmanCodes 编码表
     */
    private static void generateCodes(Node node, String code, Map<Character, String> huffmanCodes) {
        if (node == null) return;
        
        // 如果是叶节点，保存编码
        if (node.isLeaf()) {
            // 特殊情况：只有一个字符
            if (code.isEmpty()) {
                huffmanCodes.put(node.character, "0");
            } else {
                huffmanCodes.put(node.character, code);
            }
        } else {
            // 递归处理左右子树
            generateCodes(node.left, code + "0", huffmanCodes);
            generateCodes(node.right, code + "1", huffmanCodes);
        }
    }
    
    /**
     * Huffman编码
     * @param input 输入字符串
     * @return 编码结果
     */
    public static EncodeResult encode(String input) {
        if (input == null || input.isEmpty()) {
            return new EncodeResult("", new HashMap<>());
        }
        
        // 统计字符频率
        Map<Character, Integer> frequencyMap = new HashMap<>();
        for (char c : input.toCharArray()) {
            frequencyMap.put(c, frequencyMap.getOrDefault(c, 0) + 1);
        }
        
        // 构建Huffman树
        Node root = buildHuffmanTree(frequencyMap);
        
        // 生成Huffman编码表
        Map<Character, String> huffmanCodes = generateHuffmanCodes(root);
        
        // 编码输入数据
        StringBuilder encodedData = new StringBuilder();
        for (char c : input.toCharArray()) {
            encodedData.append(huffmanCodes.get(c));
        }
        
        return new EncodeResult(encodedData.toString(), huffmanCodes);
    }
    
    /**
     * Huffman解码
     * @param encodedData 编码后的数据
     * @param huffmanCodes Huffman编码表
     * @return 解码后的原始数据
     */
    public static String decode(String encodedData, Map<Character, String> huffmanCodes) {
        if (encodedData == null || encodedData.isEmpty()) {
            return "";
        }
        
        // 构建反向映射（编码到字符）
        Map<String, Character> reverseCodes = new HashMap<>();
        for (Map.Entry<Character, String> entry : huffmanCodes.entrySet()) {
            reverseCodes.put(entry.getValue(), entry.getKey());
        }
        
        // 特殊情况：只有一个字符
        if (huffmanCodes.size() == 1) {
            StringBuilder result = new StringBuilder();
            char singleChar = huffmanCodes.keySet().iterator().next();
            int length = encodedData.length();
            for (int i = 0; i < length; i++) {
                result.append(singleChar);
            }
            return result.toString();
        }
        
        // 构建Huffman树用于解码
        Node root = buildDecodeTree(huffmanCodes);
        
        // 解码
        StringBuilder result = new StringBuilder();
        Node current = root;
        
        for (char bit : encodedData.toCharArray()) {
            // 根据比特位移动到相应的子节点
            if (bit == '0') {
                current = current.left;
            } else {
                current = current.right;
            }
            
            // 如果到达叶节点，输出字符并回到根节点
            if (current.isLeaf()) {
                result.append(current.character);
                current = root;
            }
        }
        
        return result.toString();
    }
    
    /**
     * 根据编码表构建用于解码的Huffman树
     * @param huffmanCodes Huffman编码表
     * @return Huffman树根节点
     */
    private static Node buildDecodeTree(Map<Character, String> huffmanCodes) {
        Node root = new Node(0, null, null);
        
        for (Map.Entry<Character, String> entry : huffmanCodes.entrySet()) {
            char character = entry.getKey();
            String code = entry.getValue();
            
            Node current = root;
            for (int i = 0; i < code.length(); i++) {
                char bit = code.charAt(i);
                
                if (bit == '0') {
                    if (current.left == null) {
                        current.left = new Node(0, null, null);
                    }
                    current = current.left;
                } else {
                    if (current.right == null) {
                        current.right = new Node(0, null, null);
                    }
                    current = current.right;
                }
            }
            
            // 设置叶节点的字符
            current.character = character;
        }
        
        return root;
    }
    
    /**
     * 计算压缩率
     * @param original 原始数据大小（字节）
     * @param compressed 压缩后数据大小（字节）
     * @return 压缩率（百分比）
     */
    public static double calculateCompressionRatio(int original, int compressed) {
        if (original == 0) return 0;
        return (1.0 - (double) compressed / original) * 100;
    }
    
    /**
     * 测试方法
     */
    public static void main(String[] args) {
        // 测试用例1：包含重复字符的字符串
        String test1 = "ABRACADABRA";
        System.out.println("原始字符串: " + test1);
        System.out.println("原始长度: " + test1.length() + " 字符");
        
        EncodeResult encoded1 = encode(test1);
        System.out.println("Huffman编码表:");
        for (Map.Entry<Character, String> entry : encoded1.huffmanCodes.entrySet()) {
            if (entry.getKey() == '\0') {
                System.out.println("EOF: " + entry.getValue());
            } else {
                System.out.println(entry.getKey() + ": " + entry.getValue());
            }
        }
        System.out.println("编码结果: " + encoded1.encodedData);
        System.out.println("编码长度: " + encoded1.encodedData.length() + " 位");
        
        String decoded1 = decode(encoded1.encodedData, encoded1.huffmanCodes);
        System.out.println("解码结果: " + decoded1);
        System.out.println("编码解码是否正确: " + test1.equals(decoded1));
        System.out.println("压缩率: " + String.format("%.2f%%", 
            calculateCompressionRatio(test1.length() * 8, encoded1.encodedData.length())));
        System.out.println();
        
        // 测试用例2：包含重复模式的字符串
        String test2 = "AAAAABBBBBCCCCCDDDDDEEEEE";
        System.out.println("原始字符串: " + test2);
        System.out.println("原始长度: " + test2.length() + " 字符");
        
        EncodeResult encoded2 = encode(test2);
        System.out.println("Huffman编码表:");
        for (Map.Entry<Character, String> entry : encoded2.huffmanCodes.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
        System.out.println("编码结果: " + encoded2.encodedData);
        System.out.println("编码长度: " + encoded2.encodedData.length() + " 位");
        
        String decoded2 = decode(encoded2.encodedData, encoded2.huffmanCodes);
        System.out.println("解码结果: " + decoded2);
        System.out.println("编码解码是否正确: " + test2.equals(decoded2));
        System.out.println("压缩率: " + String.format("%.2f%%", 
            calculateCompressionRatio(test2.length() * 8, encoded2.encodedData.length())));
        System.out.println();
        
        // 测试用例3：随机字符串
        String test3 = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        System.out.println("原始字符串: " + test3);
        System.out.println("原始长度: " + test3.length() + " 字符");
        
        EncodeResult encoded3 = encode(test3);
        System.out.println("Huffman编码表:");
        for (Map.Entry<Character, String> entry : encoded3.huffmanCodes.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
        System.out.println("编码结果: " + encoded3.encodedData);
        System.out.println("编码长度: " + encoded3.encodedData.length() + " 位");
        
        String decoded3 = decode(encoded3.encodedData, encoded3.huffmanCodes);
        System.out.println("解码结果: " + decoded3);
        System.out.println("编码解码是否正确: " + test3.equals(decoded3));
        System.out.println("压缩率: " + String.format("%.2f%%", 
            calculateCompressionRatio(test3.length() * 8, encoded3.encodedData.length())));
    }
}