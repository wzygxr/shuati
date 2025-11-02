package class154;

// HDU 1512 Monkey King
// 题目大意：
// 有n只猴子，每只猴子有一个武力值，开始时每只猴子都是一个独立的群体
// 每次有两只猴子要打架，它们会从各自群体中找出武力值最大的猴子进行战斗
// 战斗结束后，两只猴子的武力值各自减半（向下取整），然后两个群体合并
// 如果两只猴子已经在同一个群体中，则输出-1
// 1 <= n <= 10^5
// 0 <= 武力值 <= 10^9
// 测试链接 : http://acm.hdu.edu.cn/showproblem.php?pid=1512

import java.io.*;
import java.util.*;

public class Code08_MonkeyKing {

    public static int MAXN = 100001;

    public static int n, m;

    // 每只猴子的武力值
    public static int[] power = new int[MAXN];

    // 左偏树相关数组
    public static int[] left = new int[MAXN];
    public static int[] right = new int[MAXN];
    public static int[] dist = new int[MAXN];

    // 并查集相关数组
    public static int[] father = new int[MAXN];

    // 初始化
    public static void prepare() {
        // 空节点的距离为-1
        dist[0] = -1;
        for (int i = 1; i <= n; i++) {
            // 每个节点初始时左右子树为空，距离为0
            left[i] = right[i] = dist[i] = 0;
            // 每个节点初始时自己是自己的代表节点
            father[i] = i;
        }
    }

    // 并查集查找，带路径压缩
    public static int find(int i) {
        if (father[i] == i) {
            return i;
        }
        return father[i] = find(father[i]);
    }

    // 合并两棵左偏树，维护大根堆
    public static int merge(int i, int j) {
        // 如果其中一个为空，返回另一个
        if (i == 0 || j == 0) {
            return i + j;
        }
        
        // 确保i是根节点较大的那个树
        if (power[i] < power[j]) {
            int tmp = i;
            i = j;
            j = tmp;
        }
        
        // 递归合并i的右子树和j
        right[i] = merge(right[i], j);
        
        // 维护左偏性质：左子树的距离不小于右子树的距离
        if (dist[left[i]] < dist[right[i]]) {
            int tmp = left[i];
            left[i] = right[i];
            right[i] = tmp;
        }
        
        // 更新距离
        dist[i] = dist[right[i]] + 1;
        
        // 更新父节点信息
        father[left[i]] = father[right[i]] = i;
        
        return i;
    }

    // 删除堆顶元素
    public static int pop(int i) {
        // 将左右子树的father设置为自己
        father[left[i]] = left[i];
        father[right[i]] = right[i];
        
        // 合并左右子树，作为新的根
        father[i] = merge(left[i], right[i]);
        
        // 清空当前节点信息
        left[i] = right[i] = dist[i] = 0;
        
        return father[i];
    }

    // 模拟一次战斗
    public static int fight(int x, int y) {
        // 找到x和y所在的群体代表节点
        int a = find(x);
        int b = find(y);
        
        // 如果在同一个群体，无法战斗
        if (a == b) {
            return -1;
        }
        
        // 从两个群体中取出战斗力最大的猴子
        int l = pop(a);
        int r = pop(b);
        
        // 战斗后武力值减半
        power[a] /= 2;
        power[b] /= 2;
        
        // 重新合并到左偏树中
        father[a] = father[b] = father[l] = father[r] = 
            merge(merge(l, a), merge(r, b));
        
        // 返回合并后群体的最大战斗力
        return power[father[a]];
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String line;
        
        // 多组测试数据
        while ((line = br.readLine()) != null && !line.isEmpty()) {
            n = Integer.parseInt(line);
            prepare();
            
            // 读入每只猴子的武力值
            String[] powerStr = br.readLine().split(" ");
            for (int i = 1; i <= n; i++) {
                power[i] = Integer.parseInt(powerStr[i - 1]);
            }
            
            m = Integer.parseInt(br.readLine());
            
            // 处理每次战斗
            for (int i = 1; i <= m; i++) {
                String[] xy = br.readLine().split(" ");
                int x = Integer.parseInt(xy[0]);
                int y = Integer.parseInt(xy[1]);
                System.out.println(fight(x, y));
            }
        }
    }
    
    /*
    算法分析：
    
    时间复杂度：
    1. 初始化：O(n)
    2. 合并操作：O(log n)
    3. 删除堆顶：O(log n)
    4. 查找操作：近似O(1)（由于路径压缩）
    5. 总体：O(m * log n)
    
    空间复杂度：O(n)
    
    算法思路：
    1. 使用左偏树维护每个群体，支持快速合并和删除最大值
    2. 使用并查集快速判断两只猴子是否在同一个群体
    3. 每次战斗：
       - 通过并查集判断是否在同一群体
       - 从两个群体中删除最大武力值的猴子
       - 两个猴子武力值减半后重新加入对应群体
       - 合并两个群体
    
    工程化考虑：
    1. 输入输出优化：使用BufferedReader提高读取效率
    2. 异常处理：处理多组测试数据的输入结束条件
    3. 内存管理：合理使用数组，避免动态内存分配
    4. 代码可读性：添加详细注释，清晰的变量命名
    
    与标准库对比：
    1. Java标准库中的PriorityQueue不支持高效合并操作
    2. 左偏树在合并操作上有明显优势
    3. 但在单次操作性能上可能不如优化的二叉堆
    
    调试技巧：
    1. 可以添加打印函数验证左偏树结构
    2. 注意检查并查集的路径压缩是否正确
    3. 特别关注武力值减半后的处理
    
    极端情况：
    1. 所有猴子武力值相同
    2. 只有一个猴子
    3. 所有战斗都在相同群体内（都输出-1）
    
    语言特性差异：
    1. Java中使用BufferedReader提高输入效率
    2. Java中使用System.out.println输出结果
    3. Java中使用/进行整数除法（向下取整）
    4. Java中使用String.split(" ")进行字符串分割
    */
}