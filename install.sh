#!/bin/bash

echo ""
echo ""
echo "Install V2Engine (For Unix)"
echo ""
echo ""

INSTALL_DIR="$HOME/bin"

read -p "Choose install location [default: $INSTALL_DIR]: " choice
if [ "$choice" != "" ]; then
  INSTALL_DIR="$choice"
  #why are you doing this to me, bash? I was actually liking bash until I encountered this shit...
  if [ "${INSTALL_DIR:0:2}" == "~/" ]; then
    INSTALL_DIR="${HOME}${INSTALL_DIR:1}"
  fi
fi
echo "Install directory: $INSTALL_DIR"
echo ""


if [ ! -d "$INSTALL_DIR" ]; then
  read -e -p "Directory 	$INSTALL_DIR does not exist, make it? [y/N]: " choice
  if [ "$choice" == 'y' ] || [ "$choice" == 'Y' ]; then
    mkdir "$INSTALL_DIR"
  else
    echo "Cancelling installation."
    exit 1
  fi
fi

if [ -d V2EngineTempInstallFolder ]; then
  echo "\"V2EngineTempInstallFolder\" Already exists! please delete it or run the script where it does not exist!"
  exit 1
fi
mkdir V2EngineTempInstallFolder
cd V2EngineTempInstallFolder

echo ""
echo "Downloading asset #0:"

curl -H "Accept: application/vnd.github.v3+json"   https://api.github.com/repos/NGSpace/V2Engine/releases/latest     | jq .assets[0].browser_download_url     | xargs wget -qi -

echo ""
echo "Downloading asset #1:"

curl -H "Accept: application/vnd.github.v3+json"   https://api.github.com/repos/NGSpace/V2Engine/releases/latest     | jq .assets[1].browser_download_url     | xargs wget -qi -

echo ""
echo ""
echo "Finished downloading assets, installing."

cd ..
cp V2EngineTempInstallFolder/v2engine.jar $INSTALL_DIR/v2engine.jar
cp V2EngineTempInstallFolder/v2engine.sh $INSTALL_DIR/v2engine
chmod +x $INSTALL_DIR/v2engine
echo "Finished asset installation, removing temp folder."
rm -r V2EngineTempInstallFolder

echo ""
echo ""
echo "Finished V2Engine installation."
echo "Run v2engine --help for a list of parameters and options."
echo ""