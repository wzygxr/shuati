# -*- coding: utf-8 -*-

"""
HDU 2896 病毒侵袭
题目链接：http://acm.hdu.edu.cn/showproblem.php?pid=2896
题目描述：每个病毒都有一个编号，依此为1—N。不同编号的病毒特征码不会相同。
在这之后一行，有一个整数M（1<=M<=1000），表示网站数。
接下来M行，每行表示一个网站源码，源码字符串长度在1—10000之间。
输出包含病毒特征码的网站编号和病毒编号。

算法详解：
这是一道AC自动机应用题，需要在多个文本中查找多个模式串，并记录每个文本中
包含哪些模式串。

算法核心思想：
1. 将所有病毒特征码插入到Trie树中
2. 构建失配指针（fail指针）
3. 对每个网站源码进行匹配，记录包含的病毒编号

时间复杂度分析：
1. 构建Trie树：O(∑|Pi|)，其中Pi是第i个病毒特征码
2. 构建fail指针：O(∑|Pi|)
3. 匹配：O(∑|Ti|)，其中Ti是第i个网站源码
总时间复杂度：O(∑|Pi| + ∑|Ti|)

空间复杂度：O(∑|Pi| × |Σ|)，其中Σ是字符集大小

适用场景：
1. 网站安全检测
2. 病毒特征码匹配

工程化考量：
1. 异常处理：检查输入参数的有效性
2. 性能优化：使用字典代替列表提高访问速度
3. 内存优化：合理使用Python的数据结构

与机器学习的联系：
1. 在网络安全中用于恶意代码检测
2. 在生物信息学中用于基因序列匹配
"""

from collections import deque

class TrieNode:
    def __init__(self):
        self.children = {}
        self.is_end = False
        self.fail = None  # type: ignore
        self.virus_id = -1  # 病毒编号

class VirusInvasion:
    def __init__(self):
        self.root = TrieNode()
        self.infected_websites = {}  # 每个病毒感染的网站列表
        self.website_viruses = {}  # 每个网站包含的病毒列表
    
    def build_trie(self, viruses):
        """
        构建Trie树
        :param viruses: 病毒特征码列表
        """
        for i, virus in enumerate(viruses):
            node = self.root
            for char in virus:
                if char not in node.children:
                    node.children[char] = TrieNode()
                node = node.children[char]
            node.is_end = True
            node.virus_id = i + 1  # 病毒编号从1开始
            
            # 初始化病毒感染的网站列表
            self.infected_websites[i + 1] = []
    
    def build_ac_automation(self):
        """
        构建AC自动机
        """
        # 初始化根节点的失配指针
        self.root.fail = self.root  # type: ignore
        
        queue = deque()
        # 处理根节点的子节点
        for char in self.root.children:
            child = self.root.children[char]
            child.fail = self.root
            queue.append(child)
        
        # BFS构建失配指针
        while queue:
            node = queue.popleft()
            
            for char in node.children:
                child = node.children[char]
                queue.append(child)
                
                # 查找失配指针
                fail_node = node.fail
                while char not in fail_node.children and fail_node != self.root:
                    fail_node = fail_node.fail  # type: ignore
                
                if char in fail_node.children:
                    child.fail = fail_node.children[char]
                else:
                    child.fail = self.root
    
    def match_website(self, website_id, website_code):
        """
        匹配网站源码
        :param website_id: 网站编号
        :param website_code: 网站源码
        """
        current = self.root  # type: TrieNode
        
        # 初始化网站的病毒列表
        self.website_viruses[website_id] = set()
        
        for char in website_code:
            index = char
            
            # 根据失配指针跳转
            while index not in current.children and current != self.root:
                current = current.fail  # type: ignore
            
            if index in current.children:
                current = current.children[index]
            else:
                current = self.root
            
            # 检查是否有匹配的病毒特征码
            temp = current  # type: TrieNode
            while temp != self.root:
                if temp.is_end:
                    # 记录感染的网站和病毒
                    self.infected_websites[temp.virus_id].append(website_id)
                    self.website_viruses[website_id].add(temp.virus_id)
                temp = temp.fail  # type: ignore

def main():
    # 示例输入（实际应用中需要从标准输入读取）
    viruses = ["aaa", "bbb", "ccc"]  # 病毒特征码
    websites = ["aaabbbccc", "aaabbb", "bbbccc"]  # 网站源码
    
    detector = VirusInvasion()
    
    # 构建Trie树
    detector.build_trie(viruses)
    
    # 构建AC自动机
    detector.build_ac_automation()
    
    # 匹配每个网站
    for i, website in enumerate(websites):
        detector.match_website(i + 1, website)
    
    # 输出结果
    infected_count = 0
    for i in range(1, len(websites) + 1):
        if detector.website_viruses[i]:
            infected_count += 1
            virus_list = sorted(list(detector.website_viruses[i]))
            print(f"web {i}:", end="")
            for virus_id in virus_list:
                print(f" {virus_id}", end="")
            print()
    
    print(f"total: {infected_count}")

if __name__ == "__main__":
    main()