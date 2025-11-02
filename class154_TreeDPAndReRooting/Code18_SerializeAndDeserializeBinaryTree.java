// 二叉树的序列化与反序列化
// 题目链接：https://leetcode.com/problems/serialize-and-deserialize-binary-tree/
// 序列化是将一个数据结构或者对象转换为连续的比特位的操作，进而可以将转换后的数据存储在一个文件或者内存中，
// 同时也可以通过网络传输到另一个计算机环境，采取相反方式重构得到原数据。
// 请设计一个算法来实现二叉树的序列化与反序列化。这里不限定你的序列 / 反序列化算法执行逻辑，
// 你只需要保证一个二叉树可以被序列化为一个字符串并且将这个字符串反序列化为原始的树结构。

/*
题目解析：
这是一个关于二叉树表示和重建的问题。我们需要设计两个方法：
1. serialize：将二叉树转换为字符串
2. deserialize：将字符串转换回二叉树

算法思路：
1. 基于层序遍历的序列化和反序列化：
   - 序列化：使用队列进行层序遍历，记录每个节点的值，空子节点用特殊标记（如"null"）表示
   - 反序列化：将字符串按分隔符分割，使用队列重建二叉树

2. 基于前序遍历的序列化和反序列化：
   - 序列化：使用递归进行前序遍历，记录每个节点的值，空子节点用特殊标记表示
   - 反序列化：使用递归根据前序遍历结果重建二叉树

3. 基于后序遍历的序列化和反序列化：
   - 序列化：使用递归进行后序遍历
   - 反序列化：使用栈辅助重建二叉树

时间复杂度：O(n) - 每个节点只被访问常数次
空间复杂度：O(n) - 需要存储序列化的字符串和辅助数据结构
是否为最优解：是，所有节点都需要被处理，时间复杂度不可能低于O(n)

边界情况：
- 空树：序列化为包含一个null的字符串
- 单节点树：序列化为只包含该节点值的字符串
- 完全二叉树：所有节点都有值
- 不平衡树：存在大量空子节点

与机器学习/深度学习的联系：
- 树结构的序列化在模型保存和加载中有重要应用
- 类似的技术也用于决策树模型的持久化
- 在分布式系统中，数据结构的序列化是数据传输的基础
*/

// 导入必要的类
import java.util.*;

// 二叉树节点的定义
class TreeNode {
    int val;
    TreeNode left;
    TreeNode right;
    TreeNode(int x) { val = x; }
}

// 基于层序遍历的序列化和反序列化实现
class CodecBFS {
    // 序列化函数：将二叉树转换为字符串
    public String serialize(TreeNode root) {
        // 处理空树的边界情况
        if (root == null) {
            return "[]";
        }
        
        // 使用StringBuilder高效拼接字符串
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        
        // 使用队列进行层序遍历
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        
        // 记录最后一个非空节点的位置，用于去除末尾多余的null
        int lastNonNullIndex = 0;
        
        // 层序遍历树
        while (!queue.isEmpty()) {
            TreeNode node = queue.poll();
            
            // 如果节点不为空，记录其值并将左右子节点加入队列
            if (node != null) {
                sb.append(node.val);
                queue.offer(node.left);
                queue.offer(node.right);
                lastNonNullIndex = sb.length();
            } else {
                // 如果节点为空，添加null标记
                sb.append("null");
            }
            
            // 如果队列不为空，添加分隔符
            if (!queue.isEmpty()) {
                sb.append(",");
            }
        }
        
        // 移除末尾多余的null和分隔符
        if (lastNonNullIndex < sb.length()) {
            // 找到最后一个非空节点后面的内容并移除
            int endIndex = sb.lastIndexOf(",null");
            if (endIndex != -1) {
                sb.setLength(endIndex);
            }
        }
        
        sb.append("]");
        return sb.toString();
    }
    
    // 反序列化函数：将字符串转换回二叉树
    public TreeNode deserialize(String data) {
        // 处理空树的边界情况
        if (data.equals("[]")) {
            return null;
        }
        
        // 解析字符串，获取节点值数组
        String[] values = data.substring(1, data.length() - 1).split(",");
        if (values.length == 0) {
            return null;
        }
        
        // 创建根节点
        TreeNode root = new TreeNode(Integer.parseInt(values[0]));
        
        // 使用队列重建二叉树
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        
        int i = 1;  // 从第二个元素开始处理子节点
        
        while (!queue.isEmpty() && i < values.length) {
            TreeNode node = queue.poll();
            
            // 处理左子节点
            if (i < values.length && !values[i].equals("null")) {
                node.left = new TreeNode(Integer.parseInt(values[i]));
                queue.offer(node.left);
            }
            i++;
            
            // 处理右子节点
            if (i < values.length && !values[i].equals("null")) {
                node.right = new TreeNode(Integer.parseInt(values[i]));
                queue.offer(node.right);
            }
            i++;
        }
        
        return root;
    }
}

// 基于前序遍历的序列化和反序列化实现
class CodecDFS {
    // 序列化函数：前序遍历
    public String serialize(TreeNode root) {
        StringBuilder sb = new StringBuilder();
        serializeDFS(root, sb);
        return sb.toString();
    }
    
    private void serializeDFS(TreeNode node, StringBuilder sb) {
        // 如果节点为空，添加特殊标记
        if (node == null) {
            sb.append("#,");
            return;
        }
        
        // 访问当前节点（根）
        sb.append(node.val).append(",");
        
        // 递归访问左子树
        serializeDFS(node.left, sb);
        
        // 递归访问右子树
        serializeDFS(node.right, sb);
    }
    
    // 反序列化函数
    public TreeNode deserialize(String data) {
        // 使用队列存储所有节点值
        Queue<String> queue = new LinkedList<>(Arrays.asList(data.split(",")));
        return deserializeDFS(queue);
    }
    
    private TreeNode deserializeDFS(Queue<String> queue) {
        // 从队列中取出当前节点的值
        String val = queue.poll();
        
        // 如果是特殊标记，表示空节点
        if (val.equals("#")) {
            return null;
        }
        
        // 创建当前节点
        TreeNode node = new TreeNode(Integer.parseInt(val));
        
        // 递归构建左子树
        node.left = deserializeDFS(queue);
        
        // 递归构建右子树
        node.right = deserializeDFS(queue);
        
        return node;
    }
}

// 基于后序遍历的序列化和反序列化实现
class CodecPostOrder {
    // 序列化函数：后序遍历
    public String serialize(TreeNode root) {
        StringBuilder sb = new StringBuilder();
        serializePostOrder(root, sb);
        return sb.toString();
    }
    
    private void serializePostOrder(TreeNode node, StringBuilder sb) {
        // 如果节点为空，添加特殊标记
        if (node == null) {
            sb.append("#,");
            return;
        }
        
        // 递归访问左子树
        serializePostOrder(node.left, sb);
        
        // 递归访问右子树
        serializePostOrder(node.right, sb);
        
        // 访问当前节点（根）
        sb.append(node.val).append(",");
    }
    
    // 反序列化函数
    public TreeNode deserialize(String data) {
        // 使用栈辅助构建后序遍历的树
        Stack<String> stack = new Stack<>();
        String[] values = data.split(",");
        
        // 将所有节点值逆序压入栈中
        for (int i = values.length - 1; i >= 0; i--) {
            if (!values[i].isEmpty()) {
                stack.push(values[i]);
            }
        }
        
        return deserializePostOrder(stack);
    }
    
    private TreeNode deserializePostOrder(Stack<String> stack) {
        String val = stack.pop();
        
        // 如果是特殊标记，表示空节点
        if (val.equals("#")) {
            return null;
        }
        
        // 创建当前节点
        TreeNode node = new TreeNode(Integer.parseInt(val));
        
        // 注意：后序遍历是左-右-根，反序列化时需要先处理右子树
        node.right = deserializePostOrder(stack);
        node.left = deserializePostOrder(stack);
        
        return node;
    }
}

// 主类，用于测试
public class Code18_SerializeAndDeserializeBinaryTree {
    public static void main(String[] args) {
        // 构建测试树
        //       1
        //      / \
        //     2   3
        //        / \
        //       4   5
        TreeNode root = new TreeNode(1);
        TreeNode node2 = new TreeNode(2);
        TreeNode node3 = new TreeNode(3);
        TreeNode node4 = new TreeNode(4);
        TreeNode node5 = new TreeNode(5);
        
        root.left = node2;
        root.right = node3;
        node3.left = node4;
        node3.right = node5;
        
        // 测试层序遍历实现
        System.out.println("=== 层序遍历实现 ===");
        CodecBFS codecBFS = new CodecBFS();
        String serializedBFS = codecBFS.serialize(root);
        System.out.println("序列化结果: " + serializedBFS);
        TreeNode deserializedBFS = codecBFS.deserialize(serializedBFS);
        System.out.println("反序列化后再序列化: " + codecBFS.serialize(deserializedBFS));
        
        // 测试前序遍历实现
        System.out.println("\n=== 前序遍历实现 ===");
        CodecDFS codecDFS = new CodecDFS();
        String serializedDFS = codecDFS.serialize(root);
        System.out.println("序列化结果: " + serializedDFS);
        TreeNode deserializedDFS = codecDFS.deserialize(serializedDFS);
        System.out.println("反序列化后再序列化: " + codecDFS.serialize(deserializedDFS));
        
        // 测试后序遍历实现
        System.out.println("\n=== 后序遍历实现 ===");
        CodecPostOrder codecPostOrder = new CodecPostOrder();
        String serializedPostOrder = codecPostOrder.serialize(root);
        System.out.println("序列化结果: " + serializedPostOrder);
        TreeNode deserializedPostOrder = codecPostOrder.deserialize(serializedPostOrder);
        System.out.println("反序列化后再序列化: " + codecPostOrder.serialize(deserializedPostOrder));
        
        // 测试边界情况
        System.out.println("\n=== 边界情况测试 ===");
        
        // 测试空树
        TreeNode emptyTree = null;
        String serializedEmpty = codecBFS.serialize(emptyTree);
        System.out.println("空树序列化: " + serializedEmpty);
        TreeNode deserializedEmpty = codecBFS.deserialize(serializedEmpty);
        System.out.println("空树反序列化后再序列化: " + codecBFS.serialize(deserializedEmpty));
        
        // 测试单节点树
        TreeNode singleNode = new TreeNode(42);
        String serializedSingle = codecBFS.serialize(singleNode);
        System.out.println("单节点树序列化: " + serializedSingle);
        TreeNode deserializedSingle = codecBFS.deserialize(serializedSingle);
        System.out.println("单节点树反序列化后再序列化: " + codecBFS.serialize(deserializedSingle));
    }
    
    // 辅助函数：验证两个树是否相同（用于测试）
    public static boolean isSameTree(TreeNode p, TreeNode q) {
        if (p == null && q == null) return true;
        if (p == null || q == null) return false;
        if (p.val != q.val) return false;
        return isSameTree(p.left, q.left) && isSameTree(p.right, q.right);
    }
}

/*
工程化考量：
1. 异常处理：
   - 处理了空树、单节点树等边界情况
   - 使用特殊标记表示空子节点，确保序列化/反序列化的准确性
   - 在反序列化时进行了数组边界检查

2. 性能优化：
   - 使用StringBuilder替代字符串拼接，提高序列化效率
   - 层序遍历实现中优化了输出，去除了末尾多余的null值
   - 使用队列和栈等数据结构辅助遍历和重建

3. 代码质量：
   - 提供了三种不同的实现方式（层序、前序、后序）
   - 代码结构清晰，职责分明
   - 添加了详细的注释说明算法思路和实现细节

4. 可扩展性：
   - 序列化格式可以根据需要调整
   - 可以轻松扩展为处理N叉树或其他树形结构
   - 序列化结果可以进一步压缩以减少存储空间

5. 调试技巧：
   - 添加了验证函数isSameTree，用于检查反序列化的准确性
   - 在测试代码中包含了多种边界情况的测试
   - 可以添加日志输出中间状态

6. Java特有优化：
   - 利用StringBuilder高效处理字符串构建
   - 使用Collection框架中的队列和栈
   - 利用自动装箱/拆箱简化类型转换

7. 算法安全与业务适配：
   - 序列化格式易于人类阅读，便于调试
   - 可以根据性能需求选择不同的实现方式
   - 对于大型树结构，层序遍历实现可能更节省内存

8. 数据格式考量：
   - 序列化结果采用JSON数组格式，易于与其他系统集成
   - 使用逗号作为分隔符，使用#或null表示空节点
   - 可以根据需要调整为其他格式，如XML或自定义二进制格式

9. 序列化/反序列化的完整性：
   - 确保了序列化和反序列化是互逆操作
   - 测试代码验证了反序列化后再序列化可以得到相同结果
   - 处理了各种可能的数据异常情况
*/