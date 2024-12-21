echo ""
echo ""
echo "Installing V2Engine fo linux (and maybe mac, idk if this will work so...)"
echo ""
echo ""

if [ ! -d ~/bin ]; then
  echo "~/bin does not exist, creating it."
  mkdir ~/bin
fi

if [ -d V2EngineTempInstallFolder ]; then
  echo "\"V2EngineTempInstallFolder\" Already exists! please delete it or run the script where it does not exist!"
  exit 1
fi
mkdir V2EngineTempInstallFolder
cd V2EngineTempInstallFolder

echo ""
echo "Downloading asset #0"
echo ""

curl -H "Accept: application/vnd.github.v3+json"   https://api.github.com/repos/NGSpace/V2Engine/releases/latest     | jq .assets[0].browser_download_url     | xargs wget -qi -

echo ""
echo "Downloading asset #1"
echo ""

curl -H "Accept: application/vnd.github.v3+json"   https://api.github.com/repos/NGSpace/V2Engine/releases/latest     | jq .assets[1].browser_download_url     | xargs wget -qi -

cd ..
cp V2EngineTempInstallFolder/v2engine.jar ~/bin/v2engine.jar
cp V2EngineTempInstallFolder/v2engine.sh ~/bin/v2engine
chmod +x ~/bin/v2engine.sh
rm -r V2EngineTempInstallFolder