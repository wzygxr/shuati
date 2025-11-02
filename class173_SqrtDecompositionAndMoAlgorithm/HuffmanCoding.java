package class_advanced_algorithms.huffman;

import java.util.*;

/**
 * Huffman编码实现
 * 
 * Huffman编码是一种无损数据压缩算法，它根据字符出现频率构建最优二叉树，
 * 使得出现频率高的字符具有较短的编码，出现频率低的字符具有较长的编码。
 * 
 * 算法原理：
 * 1. 统计字符频率
 * 2. 构建Huffman树：每次选择频率最小的两个节点合并
 * 3. 生成编码表：从根节点到叶节点的路径即为字符的编码
 * 4. 编码和解码过程
 * 
 * 时间复杂度：
 * - 构建Huffman树：O(n log n)，其中n是不同字符的数量
 * - 编码/解码：O(m)，其中m是字符串长度
 * 
 * 空间复杂度：O(n)
 * 
 * 优势：
 * 1. 压缩率高，能够达到信息熵的理论极限
 * 2. 实现相对简单
 * 3. 适合处理具有明显统计特性的数据
 * 
 * 劣势：
 * 1. 需要两次扫描数据（一次统计频率，一次编码）
 * 2. 对于频率分布均匀的数据压缩效果不佳
 * 3. 编码和解码需要相同的Huffman树
 * 
 * 应用场景：
 * 1. 文件压缩（如ZIP格式）
 * 2. 图像压缩（如JPEG）
 * 3. 音频压缩
 */
public class HuffmanCoding {
    
    /**
     * Huffman树节点
     * 用于表示Huffman树中的节点，包含字符、频率以及左右子树的引用
     */
    private static class Node implements Comparable<Node> {
        char character;      // 字符（仅叶节点有效，内部节点为空字符）
        int frequency;       // 频率（节点权重）
        Node left, right;    // 左右子树引用
        
        /**
         * 构造函数：创建叶节点
         * @param character 节点表示的字符
         * @param frequency 字符出现的频率
         */
        Node(char character, int frequency) {
            this.character = character;
            this.frequency = frequency;
        }
        
        /**
         * 构造函数：创建内部节点
         * @param left 左子树
         * @param right 右子树
         */
        Node(Node left, Node right) {
            this.left = left;
            this.right = right;
            this.frequency = left.frequency + right.frequency;  // 内部节点频率等于子节点频率之和
        }
        
        /**
         * 判断是否为叶节点
         * 叶节点是没有子树的节点，用于存储实际字符
         * @return true表示是叶节点，false表示是内部节点
         */
        boolean isLeaf() {
            return left == null && right == null;
        }
        
        /**
         * 实现Comparable接口，用于优先队列排序
         * 按照频率升序排列，频率相同时按照字符ASCII码排序
         * @param other 待比较的节点
         * @return 比较结果
         */
        @Override
        public int compareTo(Node other) {
            // 首先按照频率比较
            int freqCompare = Integer.compare(this.frequency, other.frequency);
            if (freqCompare != 0) {
                return freqCompare;
            }
            // 频率相同时按照字符比较，确保一致性
            return Character.compare(this.character, other.character);
        }
    }
    
    private Node root;                    // Huffman树根节点
    private Map<Character, String> codes; // 字符到编码的映射
    
    /**
     * 构造函数，根据输入字符串构建Huffman树和编码表
     * 这是Huffman编码的核心初始化方法，完成整个编码系统的构建
     * @param input 输入字符串，用于统计字符频率并构建Huffman树
     */
    public HuffmanCoding(String input) {
        // 第一步：根据输入字符串构建Huffman树
        buildHuffmanTree(input);
        // 第二步：根据构建好的Huffman树生成编码表
        buildCodeTable();
    }
    
    /**
     * 构建Huffman树
     * 使用贪心算法构建最优二叉树，使得带权路径长度最小
     * 算法步骤：
     * 1. 统计输入字符串中各字符的出现频率
     * 2. 将所有字符节点放入优先队列（最小堆）
     * 3. 重复以下操作直到队列中只剩一个节点：
     *    a. 取出频率最小的两个节点
     *    b. 创建新节点作为它们的父节点，频率为两子节点频率之和
     *    c. 将新节点放回优先队列
     * 4. 剩下的唯一节点即为Huffman树的根节点
     * @param input 输入字符串
     */
    private void buildHuffmanTree(String input) {
        // 统计字符频率：遍历字符串，使用HashMap记录每个字符的出现次数
        Map<Character, Integer> frequencyMap = new HashMap<>();
        for (char c : input.toCharArray()) {
            frequencyMap.put(c, frequencyMap.getOrDefault(c, 0) + 1);
        }
        
        // 创建优先队列（最小堆）：用于高效获取频率最小的节点
        // PriorityQueue默认使用元素的compareTo方法进行排序
        PriorityQueue<Node> pq = new PriorityQueue<>();
        for (Map.Entry<Character, Integer> entry : frequencyMap.entrySet()) {
            pq.offer(new Node(entry.getKey(), entry.getValue()));
        }
        
        // 特殊情况处理：当输入字符串只包含一种字符时
        // 为了避免编码歧义，需要构造一个高度为2的树
        if (pq.size() == 1) {
            Node node = pq.poll();
            // 构造一个根节点，其右子树为原节点
            root = new Node(null, new Node(node.character, node.frequency));
            return;
        }
        
        // 构建Huffman树：贪心算法的核心实现
        // 每次从优先队列中取出频率最小的两个节点，合并为新节点后再放回队列
        while (pq.size() > 1) {
            // 取出频率最小的两个节点作为左右子树
            Node left = pq.poll();   // 较小频率的节点作为左子树
            Node right = pq.poll();  // 较大频率的节点作为右子树
            // 创建父节点，频率为两个子节点频率之和
            Node parent = new Node(left, right);
            // 将新节点放回优先队列
            pq.offer(parent);
        }
        
        // 最后剩下的节点即为Huffman树的根节点
        root = pq.poll();
    }
    
    /**
     * 构建编码表
     * 通过遍历Huffman树为每个字符生成对应的二进制编码
     * 编码规则：从根节点到叶节点的路径，左子树为'0'，右子树为'1'
     */
    private void buildCodeTable() {
        // 初始化编码表
        codes = new HashMap<>();
        // 从根节点开始递归构建编码表，初始编码为空字符串
        buildCodeTable(root, "");
    }
    
    /**
     * 递归构建编码表
     * 使用深度优先遍历Huffman树，为每个叶节点生成对应的二进制编码
     * 编码规则：
     * - 向左子树移动时，在编码末尾添加'0'
     * - 向右子树移动时，在编码末尾添加'1'
     * - 到达叶节点时，将字符与编码的映射关系保存到编码表中
     * @param node 当前遍历到的节点
     * @param code 从根节点到当前节点的路径编码
     */
    private void buildCodeTable(Node node, String code) {
        // 递归终止条件：节点为空
        if (node == null) return;
        
        // 叶节点处理：保存字符到编码的映射关系
        if (node.isLeaf()) {
            // 特殊情况处理：当只有一个字符时，编码为"0"而非空字符串
            // 这是为了避免解码时出现歧义
            if (code.isEmpty()) {
                codes.put(node.character, "0"); // 特殊情况：只有一个字符
            } else {
                codes.put(node.character, code);
            }
            return;
        }
        
        // 递归处理左右子树
        // 向左子树移动时，在编码末尾添加'0'
        buildCodeTable(node.left, code + "0");
        // 向右子树移动时，在编码末尾添加'1'
        buildCodeTable(node.right, code + "1");
    }
    
    /**
     * Huffman编码
     * 将输入字符串转换为Huffman编码的二进制字符串
     * 时间复杂度：O(m)，其中m是输入字符串的长度
     * 空间复杂度：O(k)，其中k是编码后字符串的长度
     * @param input 待编码的输入字符串
     * @return 编码后的二进制字符串
     * @throws NullPointerException 如果输入字符串为null
     */
    public String encode(String input) {
        // 使用StringBuilder提高字符串拼接效率
        StringBuilder encoded = new StringBuilder();
        // 遍历输入字符串中的每个字符
        for (char c : input.toCharArray()) {
            // 从编码表中获取字符对应的编码并追加到结果中
            String code = codes.get(c);
            // 异常处理：检查字符是否在编码表中存在
            if (code == null) {
                throw new IllegalArgumentException("字符 '" + c + "' 未在编码表中找到");
            }
            encoded.append(code);
        }
        // 返回编码结果
        return encoded.toString();
    }
    
    /**
     * Huffman解码
     * 将Huffman编码的二进制字符串转换回原始字符串
     * 解码过程：从根节点开始，根据编码中的每一位（0或1）在Huffman树中移动
     * 当到达叶节点时，输出对应的字符并重新从根节点开始
     * 时间复杂度：O(k)，其中k是编码字符串的长度
     * 空间复杂度：O(m)，其中m是解码后字符串的长度
     * @param encoded Huffman编码的二进制字符串
     * @return 解码后的原始字符串
     * @throws IllegalArgumentException 如果编码字符串包含非法字符（非0/1）
     * @throws IllegalStateException 如果编码不完整或Huffman树未正确构建
     */
    public String decode(String encoded) {
        // 使用StringBuilder提高字符串拼接效率
        StringBuilder decoded = new StringBuilder();
        // 当前在Huffman树中的位置，初始为根节点
        Node current = root;
        
        // 遍历编码字符串中的每一位
        for (char bit : encoded.toCharArray()) {
            // 根据当前位的值在Huffman树中移动
            if (bit == '0') {
                // 遇到'0'，向左子树移动
                current = current.left;
            } else if (bit == '1') {
                // 遇到'1'，向右子树移动
                current = current.right;
            } else {
                // 异常处理：编码字符串包含非法字符
                throw new IllegalArgumentException("非法字符 '" + bit + "' 在编码中");
            }
            
            // 安全检查：确保当前节点不为空
            if (current == null) {
                throw new IllegalStateException("Huffman树结构错误或编码不完整");
            }
            
            // 到达叶节点：输出字符并重新从根节点开始
            if (current.isLeaf()) {
                decoded.append(current.character);
                current = root;
            }
        }
        
        // 返回解码结果
        return decoded.toString();
    }
    
    /**
     * 获取编码表
     * @return 编码表
     */
    public Map<Character, String> getCodeTable() {
        return new HashMap<>(codes);
    }
    
    /**
     * 计算压缩率
     * @param original 原始数据大小（位）
     * @param compressed 压缩后数据大小（位）
     * @return 压缩率（百分比）
     */
    public static double calculateCompressionRatio(int original, int compressed) {
        if (original == 0) return 0;
        return (1.0 - (double) compressed / original) * 100;
    }
    
    /**
     * 获取字符频率映射
     * @param input 输入字符串
     * @return 字符频率映射
     */
    public static Map<Character, Integer> getFrequencyMap(String input) {
        Map<Character, Integer> frequencyMap = new HashMap<>();
        for (char c : input.toCharArray()) {
            frequencyMap.put(c, frequencyMap.getOrDefault(c, 0) + 1);
        }
        return frequencyMap;
    }
    
    /**
     * 测试方法
     */
    public static void main(String[] args) {
        // 测试用例1：包含重复字符的字符串
        String test1 = "ABRACADABRA";
        System.out.println("原始字符串: " + test1);
        System.out.println("字符频率: " + getFrequencyMap(test1));
        
        HuffmanCoding hc1 = new HuffmanCoding(test1);
        String encoded1 = hc1.encode(test1);
        String decoded1 = hc1.decode(encoded1);
        
        System.out.println("编码表: " + hc1.getCodeTable());
        System.out.println("编码结果: " + encoded1);
        System.out.println("解码结果: " + decoded1);
        System.out.println("编码解码是否正确: " + test1.equals(decoded1));
        System.out.println("原始大小: " + test1.length() * 8 + " 位");
        System.out.println("压缩后大小: " + encoded1.length() + " 位");
        System.out.println("压缩率: " + String.format("%.2f%%", 
            calculateCompressionRatio(test1.length() * 8, encoded1.length())));
        System.out.println();
        
        // 测试用例2：更复杂的字符串
        String test2 = "MISSISSIPPI";
        System.out.println("原始字符串: " + test2);
        System.out.println("字符频率: " + getFrequencyMap(test2));
        
        HuffmanCoding hc2 = new HuffmanCoding(test2);
        String encoded2 = hc2.encode(test2);
        String decoded2 = hc2.decode(encoded2);
        
        System.out.println("编码表: " + hc2.getCodeTable());
        System.out.println("编码结果: " + encoded2);
        System.out.println("解码结果: " + decoded2);
        System.out.println("编码解码是否正确: " + test2.equals(decoded2));
        System.out.println("原始大小: " + test2.length() * 8 + " 位");
        System.out.println("压缩后大小: " + encoded2.length() + " 位");
        System.out.println("压缩率: " + String.format("%.2f%%", 
            calculateCompressionRatio(test2.length() * 8, encoded2.length())));
        System.out.println();
        
        // 测试用例3：均匀分布的字符串
        String test3 = "ABCDEFGH";
        System.out.println("原始字符串: " + test3);
        System.out.println("字符频率: " + getFrequencyMap(test3));
        
        HuffmanCoding hc3 = new HuffmanCoding(test3);
        String encoded3 = hc3.encode(test3);
        String decoded3 = hc3.decode(encoded3);
        
        System.out.println("编码表: " + hc3.getCodeTable());
        System.out.println("编码结果: " + encoded3);
        System.out.println("解码结果: " + decoded3);
        System.out.println("编码解码是否正确: " + test3.equals(decoded3));
        System.out.println("原始大小: " + test3.length() * 8 + " 位");
        System.out.println("压缩后大小: " + encoded3.length() + " 位");
        System.out.println("压缩率: " + String.format("%.2f%%", 
            calculateCompressionRatio(test3.length() * 8, encoded3.length())));
        System.out.println();
        
        // 测试用例4：边界情况 - 空字符串
        String test4 = "";
        System.out.println("原始字符串: '" + test4 + "'");
        try {
            HuffmanCoding hc4 = new HuffmanCoding(test4);
            System.out.println("空字符串测试: 通过");
        } catch (Exception e) {
            System.out.println("空字符串测试: 异常 - " + e.getMessage());
        }
        System.out.println();
        
        // 测试用例5：边界情况 - 单字符重复
        String test5 = "AAAAAAA";
        System.out.println("原始字符串: " + test5);
        System.out.println("字符频率: " + getFrequencyMap(test5));
        
        HuffmanCoding hc5 = new HuffmanCoding(test5);
        String encoded5 = hc5.encode(test5);
        String decoded5 = hc5.decode(encoded5);
        
        System.out.println("编码表: " + hc5.getCodeTable());
        System.out.println("编码结果: " + encoded5);
        System.out.println("解码结果: " + decoded5);
        System.out.println("编码解码是否正确: " + test5.equals(decoded5));
        System.out.println("原始大小: " + test5.length() * 8 + " 位");
        System.out.println("压缩后大小: " + encoded5.length() + " 位");
        System.out.println("压缩率: " + String.format("%.2f%%", 
            calculateCompressionRatio(test5.length() * 8, encoded5.length())));
        System.out.println();
        
        // 测试用例6：边界情况 - 两个不同字符
        String test6 = "AB";
        System.out.println("原始字符串: " + test6);
        System.out.println("字符频率: " + getFrequencyMap(test6));
        
        HuffmanCoding hc6 = new HuffmanCoding(test6);
        String encoded6 = hc6.encode(test6);
        String decoded6 = hc6.decode(encoded6);
        
        System.out.println("编码表: " + hc6.getCodeTable());
        System.out.println("编码结果: " + encoded6);
        System.out.println("解码结果: " + decoded6);
        System.out.println("编码解码是否正确: " + test6.equals(decoded6));
        System.out.println("原始大小: " + test6.length() * 8 + " 位");
        System.out.println("压缩后大小: " + encoded6.length() + " 位");
        System.out.println("压缩率: " + String.format("%.2f%%", 
            calculateCompressionRatio(test6.length() * 8, encoded6.length())));
        System.out.println();
        
        // 测试用例7：中文字符串
        String test7 = "你好世界你好";
        System.out.println("原始字符串: " + test7);
        System.out.println("字符频率: " + getFrequencyMap(test7));
        
        HuffmanCoding hc7 = new HuffmanCoding(test7);
        String encoded7 = hc7.encode(test7);
        String decoded7 = hc7.decode(encoded7);
        
        System.out.println("编码表: " + hc7.getCodeTable());
        System.out.println("编码结果: " + encoded7);
        System.out.println("解码结果: " + decoded7);
        System.out.println("编码解码是否正确: " + test7.equals(decoded7));
        System.out.println("原始大小: " + test7.length() * 16 + " 位"); // 中文字符通常占用16位
        System.out.println("压缩后大小: " + encoded7.length() + " 位");
        System.out.println("压缩率: " + String.format("%.2f%%", 
            calculateCompressionRatio(test7.length() * 16, encoded7.length())));
    }
}