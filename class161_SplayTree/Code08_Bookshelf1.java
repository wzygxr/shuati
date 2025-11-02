package class153;

/**
 * 书架 - Splay树实现，Java版本
 * 
 * 【题目来源】洛谷 P2596 [ZJOI2006]
 * 【题目链接】https://www.luogu.com.cn/problem/P2596
 * 【题目大意】
 * 维护一个书架，支持以下操作：
 * 1. Top S: 把书S放在最上面
 * 2. Bottom S: 把书S放在最下面
 * 3. Insert S T: 把书S往上移动T个位置(T<0表示下移)
 * 4. Ask S: 询问书S的排名(从0开始)
 * 5. Query k: 询问排名为k的书的编号(从0开始)
 * 
 * 【数据范围】
 * 3 <= n, m <= 8 * 10^4
 * 
 * 【算法分析】
 * 使用Splay树维护序列，支持按值和按排名的快速查找
 * 通过Splay操作将访问的节点移动到根附近优化后续访问
 * 
 * 【时间复杂度】
 * - 所有操作均摊时间复杂度为O(log n)
 * - 单次操作最坏情况可能达到O(n)
 * 
 * 【空间复杂度】O(n)
 * 
 * 【实现特点】
 * - 使用数组模拟节点结构，避免对象创建开销
 * - 添加哨兵节点简化边界情况处理
 * - 实现位置映射数组快速定位节点
 */

import java.io.*;
import java.util.*;

/**
 * Splay树实现书架问题
 * 支持书籍位置的动态维护和查询操作
 * 
 * 【核心思想】
 * 1. 使用Splay树维护书籍的顺序关系
 * 2. 通过位置映射数组实现O(1)按值查找
 * 3. 利用Splay操作优化频繁访问节点的访问速度
 * 4. 添加哨兵节点处理边界情况
 * 
 * 【应用场景】
 * - 动态维护序列中元素位置的操作
 * - 需要频繁查询元素排名和按排名查找元素的问题
 * - 算法竞赛中的数据结构问题
 */

public class Code08_Bookshelf1 {
    public static int MAXN = 80010;
    
    // Splay树节点相关数组
    public static int[] bookId = new int[MAXN];   // 书的编号
    public static int[] father = new int[MAXN];   // 父节点
    public static int[] left = new int[MAXN];     // 左子节点
    public static int[] right = new int[MAXN];    // 右子节点
    public static int[] size = new int[MAXN];     // 子树大小
    
    // 位置映射: pos[id]表示书id在Splay树中的节点编号
    public static int[] pos = new int[MAXN];
    
    public static int head = 0;  // 树根
    public static int cnt = 0;   // 节点计数
    
    /**
     * 【自底向上维护】更新节点子树大小
     * 时间复杂度: O(1)
     * 
     * @param i 需要更新的节点索引
     */
    // 更新节点信息
    public static void up(int i) {
        size[i] = size[left[i]] + size[right[i]] + 1;
    }
    
    /**
     * 【方向判断】确定节点i是其父节点的左子节点还是右子节点
     * 时间复杂度: O(1)
     * 
     * @param i 需要判断的节点索引
     * @return 1表示右子节点，0表示左子节点
     */
    // 判断节点i是其父节点的左儿子还是右儿子
    public static int lr(int i) {
        return right[father[i]] == i ? 1 : 0;
    }
    
    /**
     * 【核心旋转操作】将节点i旋转至其父节点的位置
     * 这是Splay树维护平衡的基本操作
     * 时间复杂度: O(1)
     * 空间复杂度: O(1)
     * 
     * @param i 需要旋转的节点索引
     */
    // 旋转操作
    public static void rotate(int i) {
        int f = father[i], g = father[f], soni = lr(i), sonf = lr(f);
        
        // 【旋转逻辑】根据当前节点是左子还是右子执行不同的旋转操作
        if (soni == 1) {       // 右子节点，执行右旋
            right[f] = left[i];
            if (right[f] != 0) father[right[f]] = f;
            left[i] = f;
        } else {               // 左子节点，执行左旋
            left[f] = right[i];
            if (left[f] != 0) father[left[f]] = f;
            right[i] = f;
        }
        
        // 更新祖父节点的子节点指针
        if (g != 0) {
            if (sonf == 1) right[g] = i;
            else left[g] = i;
        }
        
        // 更新父指针
        father[f] = i;
        father[i] = g;
        
        // 【重要】更新节点信息，先更新被旋转的父节点，再更新当前节点
        up(f);
        up(i);
    }
    
    /**
     * 【核心伸展操作】将节点i旋转到goal的子节点位置
     * 如果goal为0，则将i旋转到根节点
     * 这是Splay树的核心操作，通过一系列旋转使被访问节点移动到树的顶部
     * 时间复杂度: 均摊O(log n)，最坏情况O(n)
     * 空间复杂度: O(1)
     * 
     * @param i 需要旋转的节点索引
     * @param goal 目标父节点索引
     */
    // Splay操作，将节点i旋转到goal下方
    public static void splay(int i, int goal) {
        int f = father[i], g = father[f];
        
        // 当当前节点的父节点不是目标节点时，继续旋转
        while (f != goal) {
            // 【旋转策略】根据Zig-Zig和Zig-Zag情况选择不同的旋转顺序
            if (g != goal) {
                // 如果父节点和当前节点在同侧，先旋转父节点（Zig-Zig情况）
                // 否则直接旋转当前节点（Zig-Zag情况）
                if (lr(i) == lr(f)) rotate(f);
                else rotate(i);
            }
            // 最后旋转当前节点
            rotate(i);
            
            // 更新父节点和祖父节点
            f = father[i];
            g = father[f];
        }
        
        // 如果旋转到根节点，更新根节点指针
        if (goal == 0) head = i;
    }
    
    /**
     * 【查找操作】在整棵树中找到中序遍历排名为rank的节点
     * 时间复杂度: O(log n)
     * 
     * @param rank 目标排名（从1开始）
     * @return 对应排名的节点索引
     */
    // 查找中序排名为rank的节点
    public static int find(int rank) {
        int i = head;
        while (i != 0) {
            if (size[left[i]] + 1 == rank) return i;
            else if (size[left[i]] >= rank) i = left[i];
            else {
                rank -= size[left[i]] + 1;
                i = right[i];
            }
        }
        return 0; // 未找到对应排名的节点
    }
    
    /**
     * 【按书号查找】通过书号查找其在Splay树中的节点编号
     * 时间复杂度: O(1)
     * 
     * @param id 书的编号
     * @return 书id在Splay树中的节点编号
     */
    // 查找书id的节点编号
    public static int findBook(int id) {
        return pos[id];
    }
    
    /**
     * 【构建树】根据数组构建初始Splay树
     * 时间复杂度: O(n log n)
     * 空间复杂度: O(n)
     * 
     * @param books 初始书籍排列数组
     * @param n 书籍数量
     */
    // 构建初始序列
    public static void build(int[] books, int n) {
        // 添加哨兵节点
        // 【边界处理】使用哨兵节点简化边界情况处理
        bookId[++cnt] = 0;
        size[cnt] = 1;
        head = cnt;
        
        // 逐个插入书籍节点
        for (int i = 1; i <= n; i++) {
            bookId[++cnt] = books[i];
            size[cnt] = 1;
            pos[books[i]] = cnt;  // 建立书籍编号到节点编号的映射
            father[cnt] = head;
            right[head] = cnt;
            splay(cnt, 0); // 每次插入后splay到根节点
        }
        
        // 添加尾部哨兵节点
        bookId[++cnt] = 0;
        size[cnt] = 1;
        father[cnt] = head;
        right[head] = cnt;
        splay(cnt, 0);
    }
    
    /**
     * 【Top操作】把书S放在最上面
     * 时间复杂度: 均摊O(log n)
     * 
     * @param s 要放在最上面的书籍编号
     */
    // Top操作：把书S放在最上面
    public static void top(int s) {
        // 找到书籍s对应的节点并旋转到根
        int node = findBook(s);
        splay(node, 0);
        
        // 将书s从当前位置移除
        if (left[node] == 0) {
            // 没有左子树，直接用右子树替换
            head = right[node];
            father[head] = 0;
        } else if (right[node] == 0) {
            // 没有右子树，直接用左子树替换
            head = left[node];
            father[head] = 0;
        } else {
            // 同时存在左右子树
            int l = left[node];
            int r = right[node];
            left[node] = right[node] = 0;
            father[l] = father[r] = 0;
            
            // 找到左子树的最右节点（即左子树中的最大节点）
            while (right[l] != 0) l = right[l];
            splay(l, 0);
            
            // 将右子树挂载到左子树的最大节点下
            right[l] = r;
            father[r] = l;
            up(l);
            head = l;
        }
        
        // 将书s放到最上面
        left[node] = head;
        father[head] = node;
        right[node] = 0;
        father[node] = 0;
        up(node);
        up(head);
        head = node;
    }
    
    /**
     * 【Bottom操作】把书S放在最下面
     * 时间复杂度: 均摊O(log n)
     * 
     * @param s 要放在最下面的书籍编号
     */
    // Bottom操作：把书S放在最下面
    public static void bottom(int s) {
        // 找到书籍s对应的节点并旋转到根
        int node = findBook(s);
        splay(node, 0);
        
        // 将书s从当前位置移除
        if (left[node] == 0) {
            // 没有左子树，直接用右子树替换
            head = right[node];
            father[head] = 0;
        } else if (right[node] == 0) {
            // 没有右子树，直接用左子树替换
            head = left[node];
            father[head] = 0;
        } else {
            // 同时存在左右子树
            int l = left[node];
            int r = right[node];
            left[node] = right[node] = 0;
            father[l] = father[r] = 0;
            
            // 找到左子树的最右节点（即左子树中的最大节点）
            while (right[l] != 0) l = right[l];
            splay(l, 0);
            
            // 将右子树挂载到左子树的最大节点下
            right[l] = r;
            father[r] = l;
            up(l);
            head = l;
        }
        
        // 将书s放到最下面
        right[node] = head;
        father[head] = node;
        left[node] = 0;
        father[node] = 0;
        up(node);
        up(head);
        head = node;
    }
    
    /**
     * 【Insert操作】把书S往上移动T个位置
     * 时间复杂度: 均摊O(log n)
     * 
     * @param s 要移动的书籍编号
     * @param t 移动的位置数（正数表示上移，负数表示下移）
     */
    // Insert操作：把书S往上移动T个位置
    public static void insert(int s, int t) {
        if (t == 0) return;
        
        // 获取当前书籍的排名
        int rank = ask(s);
        int newRank = rank + t;
        
        // 【边界处理】处理边界情况
        if (newRank < 0) newRank = 0;
        if (newRank >= size[head] - 2) newRank = size[head] - 3;  // 减去两个哨兵节点
        
        if (newRank == rank) return;
        
        // 找到书籍s对应的节点并旋转到根
        int node = findBook(s);
        splay(node, 0);
        
        // 将书s从当前位置移除
        if (left[node] == 0) {
            // 没有左子树，直接用右子树替换
            head = right[node];
            father[head] = 0;
        } else if (right[node] == 0) {
            // 没有右子树，直接用左子树替换
            head = left[node];
            father[head] = 0;
        } else {
            // 同时存在左右子树
            int l = left[node];
            int r = right[node];
            left[node] = right[node] = 0;
            father[l] = father[r] = 0;
            
            // 找到左子树的最右节点（即左子树中的最大节点）
            while (right[l] != 0) l = right[l];
            splay(l, 0);
            
            // 将右子树挂载到左子树的最大节点下
            right[l] = r;
            father[r] = l;
            up(l);
            head = l;
        }
        
        // 将书s插入到新位置
        if (newRank == 0) {
            // 插入到最上面
            left[node] = head;
            father[head] = node;
            right[node] = 0;
            father[node] = 0;
            up(node);
            up(head);
            head = node;
        } else {
            // 找到新位置的前驱节点
            int pred = find(newRank + 1);  // +1因为有哨兵节点
            splay(pred, 0);
            
            if (right[pred] == 0) {
                // 插入到pred的右子树
                right[pred] = node;
                father[node] = pred;
                up(pred);
            } else {
                // 找到pred右子树的最左节点
                int rightChild = right[pred];
                while (left[rightChild] != 0) rightChild = left[rightChild];
                splay(rightChild, pred);
                
                // 将书s连接为rightChild的左子节点
                left[rightChild] = node;
                father[node] = rightChild;
                up(rightChild);
                up(pred);
            }
        }
    }
    
    /**
     * 【Ask操作】询问书S的排名
     * 时间复杂度: 均摊O(log n)
     * 
     * @param s 要查询排名的书籍编号
     * @return 书籍s的排名（从0开始）
     */
    // Ask操作：询问书S的排名
    public static int ask(int s) {
        // 找到书籍s对应的节点并旋转到根
        int node = findBook(s);
        splay(node, 0);
        
        // 返回左子树大小减1（因为有哨兵节点）
        return size[left[node]] - 1;  // -1因为有哨兵节点
    }
    
    /**
     * 【Query操作】询问排名为k的书的编号
     * 时间复杂度: 均摊O(log n)
     * 
     * @param k 要查询的排名（从0开始）
     * @return 排名为k的书籍编号
     */
    // Query操作：询问排名为k的书的编号
    public static int query(int k) {
        // 找到排名为k+2的节点并旋转到根（+2因为有两个哨兵节点）
        int node = find(k + 2);  // +2因为有两个哨兵节点
        splay(node, 0);
        
        // 返回节点存储的书籍编号
        return bookId[node];
    }
    
    /**
     * 【主函数】处理输入输出和操作调用
     * 【输入输出优化】使用BufferedReader和PrintWriter提高读取效率
     * 
     * @param args 命令行参数
     * @throws IOException 输入输出异常
     */
    public static void main(String[] args) throws IOException {
        // 【IO优化】使用BufferedReader和PrintWriter提高读取效率
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        // 读取书籍数量和操作数量
        String[] parts = br.readLine().split(" ");
        int n = Integer.parseInt(parts[0]);
        int m = Integer.parseInt(parts[1]);
        
        // 读取初始书籍排列
        int[] books = new int[n + 1];
        parts = br.readLine().split(" ");
        for (int i = 1; i <= n; i++) {
            books[i] = Integer.parseInt(parts[i - 1]);
        }
        
        // 构建初始Splay树
        build(books, n);
        
        // 处理每个操作
        for (int i = 0; i < m; i++) {
            parts = br.readLine().split(" ");
            String op = parts[0];
            
            if (op.equals("Top")) {
                // Top操作：把书S放在最上面
                int s = Integer.parseInt(parts[1]);
                top(s);
            } else if (op.equals("Bottom")) {
                // Bottom操作：把书S放在最下面
                int s = Integer.parseInt(parts[1]);
                bottom(s);
            } else if (op.equals("Insert")) {
                // Insert操作：把书S往上移动T个位置
                int s = Integer.parseInt(parts[1]);
                int t = Integer.parseInt(parts[2]);
                insert(s, t);
            } else if (op.equals("Ask")) {
                // Ask操作：询问书S的排名
                int s = Integer.parseInt(parts[1]);
                out.println(ask(s));
            } else if (op.equals("Query")) {
                // Query操作：询问排名为k的书的编号
                int k = Integer.parseInt(parts[1]);
                out.println(query(k));
            }
        }
        
        // 【工程化考量】确保所有输出都被刷新并关闭资源
        out.flush();
        out.close();
        br.close();
    }
}