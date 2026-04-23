# 欧拉路径全平台题库

## 目录结构说明

此仓库包含全球主流OJ平台的欧拉路径相关题目，覆盖基础题、进阶题、变种题、笔试面试真题。

### 平台分类
- 国内核心OJ
  - 力扣-LeetCode
  - 洛谷
  - 牛客网
  - AcWing
  - PTA
- 高校专属OJ
  - 北大POJ
  - 杭电HDU
  - 浙大ZOJ
- 国际主流平台
  - Codeforces
  - AtCoder
  - UVa
- 综合分类
  - 有向图欧拉路径
  - 无向图欧拉路径
  - 混合图欧拉路径
  - 应用题

### 题目类型
1. **基础题**：无向图/有向图欧拉路径/回路的判定、裸欧拉路径构造（Hierholzer算法）
2. **进阶题**：带权图的欧拉路径、混合图欧拉路径、欧拉路径与二分图/拓扑排序/并查集的结合
3. **应用题**：字符串拼接、词链、密码破解等实际应用场景

---

## 当前收录题目统计

| 类型 | 题目数量 | 平台 |
|------|----------|------|
| 有向图欧拉路径 | 2 | 计蒜客/洛谷 |
| 无向图欧拉路径 | 2 | 计蒜客/洛谷 |
| 合法排列数对 | 2 | LeetCode |
| 词链 | 2 | 洛谷 |
| 字母对 | 2 | POJ/SGU |
| 密码破解 | 2 | LeetCode/POJ |
| 太鼓达人 | 2 | 洛谷/LOJ |
| 垃圾车 | 2 | POI |
| 邮递员 | 2 | POI |
| 丁香之路 | 2 | IOI |

---

## 算法模板

### 有向图欧拉路径判定
```java
public static int directedStart() {
    int start = -1, end = -1;
    for (int i = 1; i <= n; i++) {
        int v = outDeg[i] - inDeg[i];
        if (v < -1 || v > 1 || (v == 1 && start != -1) || (v == -1 && end != -1)) {
            return -1;
        }
        if (v == 1) {
            start = i;
        }
        if (v == -1) {
            end = i;
        }
    }
    if ((start == -1) ^ (end == -1)) {
        return -1;
    }
    if (start != -1) {
        return start;
    }
    for (int i = 1; i <= n; i++) {
        if (outDeg[i] > 0) {
            return i;
        }
    }
    return -1;
}
```

### 无向图欧拉路径判定
```java
public static int undirectedStart() {
    int odd = 0;
    for (int i = 1; i <= n; i++) {
        if ((deg[i] & 1) == 1) {
            odd++;
        }
    }
    if (odd != 0 && odd != 2) {
        return -1;
    }
    for (int i = 1; i <= n; i++) {
        if (odd == 0 && deg[i] > 0) {
            return i;
        }
        if (odd == 2 && (deg[i] & 1) == 1) {
            return i;
        }
    }
    return -1;
}
```

### Hierholzer算法（迭代版）
```java
public static void euler(int node) {
    stacksize = 0;
    push(node);
    while (stacksize > 0) {
        pop();
        if (cur[u] != 0) {
            int e = cur[u];
            cur[u] = nxt[e];
            push(u);
            push(to[e]);
        } else {
            path[++cntp] = u;
        }
    }
}
```

---

## 机器学习/深度学习关联

欧拉路径在机器学习中的应用：
1. **图结构数据预处理**：将图转化为序列特征，适配RNN/Transformer等模型输入
2. **图特征提取**：基于欧拉路径的路径长度、边权分布特征在GNN中的应用
3. **图嵌入**：通过欧拉路径遍历提取图的全局特征，提升模型泛化能力

---

## 笔试面试高频提问清单

1. 无向图/有向图欧拉路径/回路的判定定理分别是什么？
2. Hierholzer算法的核心思想是什么？递归实现和迭代实现各有什么优缺点？
3. 欧拉路径构造过程中，为什么回溯时才将节点加入路径？
4. 欧拉路径与哈密顿路径的核心区别是什么？
5. 处理带重边/自环的图时，欧拉路径的判定和构造需要注意什么？

---