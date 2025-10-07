#!/usr/bin/env bash
set -e

# Colors
GREEN="\033[0;32m"
CYAN="\033[0;36m"
YELLOW="\033[1;33m"
RED="\033[0;31m"
NC="\033[0m" # No Color

for version in versions/*; do
    if [ -d "$version" ]; then
        ver=$(basename "$version")
        echo -e "${CYAN}==============================${NC}"
        echo -e "${YELLOW}Processing version ${GREEN}$ver${NC}${YELLOW}...${NC}"

        if ./gradlew "stonecutterSwitchTo$ver"; then
            echo -e "${GREEN}Switching complete for $ver!${NC}"
        else
            echo -e "${RED}Switching failed for $ver!${NC}"
            exit 1
        fi
        
        echo -e "${GREEN}Running datagen for $ver...${NC}"
        if ./gradlew "$ver:runDatagen"; then
            echo -e "${GREEN}Datagen complete for $ver!${NC}"
        else
            echo -e "${RED}Datagen failed for $ver!${NC}"
            exit 1
        fi

        echo -e "${GREEN}Building $ver...${NC}"
        if ./gradlew "$ver:build"; then
            echo -e "${GREEN}Build complete for $ver!${NC}"
        else
            echo -e "${RED}Build failed for $ver!${NC}"
            exit 1
        fi

        echo -e "${CYAN}==============================${NC}\n"
    fi
done

