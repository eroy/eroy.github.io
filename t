#!/usr/bin/env bash

case "$1" in
	en)
	with="en"
	to="ru"
	;;
	ru)
	with="ru"
	to="en"
	;;
	*)
	echo -n "
Enter please language:
en - translate with en to ru 
ru - translate with ru to en 
"
	;;
esac

inputText="${@}"
text=`echo "$inputText" | cut -c 3-`

textLen=`echo -n "${text}" | wc -c`
if [[ ${textLen} = 0 ]]; then
	echo -e "Error input text"
	exit 1
fi

translate="$(wget -U "Mozilla/5.0" -qO - "http://translate.googleapis.com/translate_a/single?client=gtx&sl=$with&tl=$to&dt=t&q=$(echo "${text}" | sed "s/[\"'<>]//g")" | sed "s/,,,0]],,.*//g" | awk -F'"' '{print $2}')"
echo $translate
echo "$translate" | xclip -selection clipboard # копировать перевод в буфер
# notify-send -u critical "$text" "$translate" # вывести уведомление
