#'mp3' => 'mp4'
$latest = Get-ChildItem -Path "./" |Where-Object {$_.Name -like "*.mp3"}| Sort-Object LastAccessTime -Descending | Select-Object -First 1
$latest.name 
#'.2.mp3' => 'm4a'
#Start-Process ffmpeg -ArgumentList '-i', 'zh-cn-1.2.mp3', 'zh-cn-1.2.flac' 
$audioName = $latest.name.Substring(0,$latest.name.Length-4)+'.2.mp3'
#'$audioName' => '-c', 'copy', $audioName
echo $audioName
Start-Process ffmpeg -ArgumentList '-i', $latest.name, '-c', 'copy', $audioName
Start-Process D:\apps\Java\jdk1.8.0_152\bin\java -ArgumentList '-Dfile.encoding=UTF-8', '-jar', 'lfasr-sdk-demo.jar', $audioName, '0', '0'

#ffmpeg -i zh-cn-1.2.mp3 -c copy zh-cn-1.2.m4a
#ffmpeg -i fahua.6.mp4 -c copy fahua.6.m4a //why m4a file size is larger than the mp4 file?
