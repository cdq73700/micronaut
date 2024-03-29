# 環境構築

terminal

## ubuntu 環境のコピー

```
wsl --export Ubuntu-22.04 "C:\wsl\Ubuntu-22.04.tar"
```

## unbuntu 環境の複製

```
wsl --import Ubuntu-22.04-local-Practice "C:\wsl\Ubuntu-22.04-local-Practice" "C:\wsl\Ubuntu-22.04.tar"
```

Ubuntu 22.04.1 LTS

## 更新

```
apt update
apt dist-upgrade
apt autoremove
```

## 古い docker バージョンの削除

```
apt-get remove docker docker-engine docker.io containerd runc
```

## docker リポジトリの設定

### 必要なパッケージをインストールします。

```
apt-get install \
    ca-certificates \
    curl \
    gnupg \
    lsb-release
```

### Docker の公式 GPG キーを取得する

```
mkdir -p /etc/apt/keyrings
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo gpg --dearmor -o /etc/apt/keyrings/docker.gpg
```

### リポジトリの登録

```
echo \
  "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.gpg] https://download.docker.com/linux/ubuntu \
  $(lsb_release -cs) stable" | sudo tee /etc/apt/sources.list.d/docker.list > /dev/null
```

## Docker のインストール

```
apt-get update
apt-get install docker-ce docker-ce-cli containerd.io docker-compose-plugin
```

## ibtables の整合性の確保

```
update-alternatives --set iptables /usr/sbin/iptables-legacy
update-alternatives --set ip6tables /usr/sbin/ip6tables-legacy
```

## make インストール

```
apt install make
```

## docker サービス開始

```
service docker start
```

## その他設定

1. 秘密鍵・公開鍵の設定
2. 秘密鍵の権限設定

```
chmod 600 ./.ssh/id_rsa
```

3. git グローバル設定

```
git config --global user.name ユーザー名
git config --global user.email ユーザーメール
```

## git クローン

```
git clone git@github.com:cdq73700/micronaut.git micronaut
```

## VSCode 実行

```
code .
```

## コンテナ実行

```
make up
```

## ホットリロード

```
make backend

./gradlew run -t
```
