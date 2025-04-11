## 同步此仓库
有至少三种方法可将此仓库内的文件同步到你的电脑上。

### 方法一：Git
1. 安装[git](https://git-scm.com/downloads)
2. 打开一个命令行窗口并转到你想要存放此仓库文件的父文件夹
3. 输入`git clone https://github.com/CPT202-2025-G22/ReadingNotes.git`并回车（需要开着梯子）

### 方法二：下载压缩包
1. 点击绿色的“Code”按钮
2. 点击“Download ZIP”

### 方法三：GitHub Desktop
1. 安装[GitHub Desktop](https://desktop.github.com/download/)
2. 运行GitHub Desktop并登录（需要开着梯子）
3. 在软件界面按下Ctrl+Shift+O，或点击菜单栏中的“File -> Clone repository...”
4. 在出现的窗口中选择“URL”，在第一个空填入`https://github.com/CPT202-2025-G22/ReadingNotes.git`
5. 选择要下载到的本地路径，然后点击“Clone”


## APIs
标有*号的是管理员执行的操作。

### User API
| Operation  | Path       | Method |
|------------|------------|--------|
| Register   | /user      | POST   |
| Log in     | /login     | POST   |
| View info  | /user/{id} | GET    |
| Modify     | /user/{id} | PUT    |
| Delete     | /user/{id} | DELETE |
| List*      | /user      | GET    |
| (Un)Block* | /user/{id} | PUT    |

### Reading log API
| Operation   | Path         | Method |
|-------------|--------------|--------|
| List log    | /log         | GET    |
| Filter log  | /log         | POST   |
| Create log  | /log         | POST   |
| Read log    | /log/{logid} | GET    |
| Edit log    | /log/{logid} | POST   |
| Delete log  | /log/{logid} | DELETE |
