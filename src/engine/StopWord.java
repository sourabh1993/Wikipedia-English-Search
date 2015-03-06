package engine;

import java.util.HashMap;
import java.util.Map;

public class StopWord {

    static String freq_words[] = {
        "absolute","accept","account","achieve","act","active","actual","add","address","admit","advertise",
        "affect","afternoon","again","against","age","ago","agree","air","allow","along","already",
        "alright","although","always","amount","another","answer","apart","apparent","appear","apply",
        "approach","appropriate","area","argue","arm","around","arrange","art","ask","associate",
        "assume","attend","authority","available","aware","away","awful","baby","back","bad","bag","balance","ball",
        "bank","bar","base","basis","bear","beat","beauty","become","bed","before","begin","behind","believe","benefit",
        "best","bet","between","big","bill","birth","bit","black","bloke","blood","blow","blue","board","boat","body",
        "book","both","bother","bottle","bottom","box","boy","break","brief","brilliant","bring","brother",
        "budget","build","bus","business","busy","buy","cake","call","car","card","care","carry","case","cat","catch",
        "cause","cent","centre","certain","chair","chairman","chance","change","chap","character","charge","cheap",
        "check","child","choice","choose","Christ","Christmas","church","city","claim","class","clean","clear","client",
        "clock","close","closes","clothe","club","coffee","cold","colleague","collect","college","colour","come",
        "comment","commit","committee","common","community","company","compare","complete","compute","concern",
        "condition","confer","consider","consult","contact","continue","contract","control","converse","cook","copy",
        "corner","correct","cost","council","count","country","county","couple","course","court","cover","create",
        "cross","cup","current","cut","dad","danger","date","day","dead","deal","debate","decide","decision","deep",
        "definite","degree","department","depend","describe","design","detail","develop","die","difference","difficult",
        "dinner","direct","discuss","district","divide","doctor","document","dog","door","double","doubt","down","draw",
        "dress","drink","drive","drop","dry","due","during","each","early","east","easy","eat","economy","educate",
        "effect","egg","eight","elect","electric","eleven","employ","encourage","end","engine","english","enjoy",
        "enough","enter","environment","equal","especial","even","evening","evidence","exact","example",
        "except","excuse","exercise","exist","expect","expense","experience","explain","express","extra","eye","face",
        "fact","fair","fall","family","far","farm","fast","father","favour","feed","feel","few","field","fight","figure"
        ,"file","fill","film","final","finance","find","fine","finish","fire","first","fish","fit","five","flat","floor",
        "fly","follow","food","foot","force","forget","form","fortune","forward","four","free","friend",
        "front","full","fun","function","fund","further","future","game","garden","gas","general","girl","give",
        "glass","go","god","good","goodbye","govern","grand","grant","great","green","ground","group","grow","guess","guy",
        "hair","half","hall","hand","hang","happen","happy","hard","hate","head","health","hear","heart","heat","heavy",
        "hell","help","here","high","history","hit","hold","holiday","home","honest","hope","horse","hospital","hot","hour",
        "house","hullo","hundred","husband","idea","identify","imagine","important","improve","include","income","increase",
        "indeed","individual","industry","inform","inside","instead","insure","interest","introduce","invest","involve",
        "issue","item","job","join","judge","jump","keep","key","kid","kill","kind","king","kitchen","knock","know",
        "labour","lad","lady","land","language","large","last","late","laugh","law","lay","lead","learn","leave","left","leg",
        "less","letter","level","lie","life","light","limit","line","link","list","listen","little","live","load","local",
        "lock","long","look","lord","lose","lot","love","low","luck","lunch","machine","index","major","make","man",
        "manage","many","mark","market","marry","match","matter","maybe","mean","meaning","measure","meet","member","mention",
        "middle","mile","milk","million","mind","minister","minus","minute","miss","mister","moment","money","month",
        "more","morning","mother","motion","move","mrs","much","music","name","nation","nature","near","necessary","need",
        "never","new","news","next","nice","night","nine","non","none","normal","north","note","notice","now","number",
        "obvious","occasion","odd","offer","office","okay","old","once","one","open","operate","opportunity","oppose","order",
        "organize","original","otherwise","ought","out","over","pack","page","paint","pair","paper","paragraph","pardon",
        "parent","park","part","particular","party","pass","past","pay","pence","pension","people","per","percent","perfect",
        "perhaps","period","person","photograph","pick","picture","piece","place","plan","play","please","plus","point",
        "police","policy","politic","poor","position","positive","possible","post","pound","power","practise","prepare",
        "present","press","pressure","presume","pretty","previous","price","print","private","probable","problem","proceed"
        ,"process","produce","product","programme","project","proper","propose","protect","provide","public","pull","purpose",
        "push","put","quality","quarter","question","quick","quid","quiet","quite","radio","rail","raise","range","rate",
        "read","ready","real","realise","really","reason","receive","recent","reckon","recognize","recommend","record","red",
        "reduce","refer","regard","region","relation","remember","report","represent","require","research","resource",
        "respect","responsible","rest","result","return","rid","right","ring","rise","road","role","roll","room","round",
        "rule","run","safe","sale","same","save","scheme","school","science","score","seat","second",
        "secretary","section","secure","see","seem","self","sell","send","sense","separate","serious","serve","service","set",
        "settle","seven","sex","shall","share","sheet","shoe","shoot","shop","short","show","shut","sick","side","sign",
        "similar","simple","sing","single","sir","sister","sit","site","situate","six","size","sleep","slight","slow",
        "small","smoke","social","society","son","soon","sorry","sort","sound","south","space","speak","special","specific",
        "speed","spell","spend","square","staff","stage","stairs","stand","standard","start","state","station","stay",
        "step","stick","still","stop","story","straight","strategy","street","strike","strong","structure","student","study",
        "stuff","stupid","subject","succeed","such","sudden","suggest","suit","summer","sun","supply","support",
        "suppose","sure","surprise","switch","system","table","take","talk","tape","tax","tea","teach","team","telephone",
        "television","tell","ten","tend","term","terrible","test","thank","therefore","thing","think","thirteen","thirty",
        "though","thousand","three","through","throw","tie","time","today","together","tomorrow","tonight","top",
        "total","touch","toward","town","trade","traffic","train","transport","travel","treat","tree","trouble","true",
        "trust","try","turn","twelve","twenty","two","type","under","understand","union","unit","unite","university",
        "unless","until","up","upon","use","usual","value","various","very","video","view","village","visit","vote","wage",
        "wait","walk","wall","want","war","warm","wash","waste","watch","water","way","wear","wee","week","weigh",
        "welcome","well","west","whether","white","whole","wide","wife","win","wind","window","wish","within","without",
        "woman","wonder","wood","word","work","world","worry","worse","worth","write","wrong","year","yes","yesterday","young"
    };

static String stop_words[] = {
    "a","about","above","across","after","again","against","all","almost","alone","along","already","also",
    "although","always","among","an","and","another","any","anybody","anyone","anything","anywhere","are","area",
    "areas","around","as","ask","asked","asking","asks","at","away","b","back","backed","backing","backs","be",
    "became","because","become","becomes","been","before","began","behind","being","beings","best","better",
    "between","big","both","but","by","c","came","can","cannot","case","cases","certain","certainly","clear",
    "clearly","come","could","d","did","differ","different","differently","do","does","done","down","down",
    "downed","downing","downs","during","e","each","early","either","end","ended","ending","ends","enough",
    "even","evenly","ever","every","everybody","everyone","everything","everywhere","f","face","faces","fact",
    "facts","far","felt","few","find","finds","first","for","four","from","full","fully","further","furthered",
    "furthering","furthers","g","gave","general","generally","get","gets","give","given","gives","go","going",
    "good","goods","got","great","greater","greatest","group","grouped","grouping","groups","h","had","has","have",
    "having","he","her","here","herself","high","high","high","higher","highest","him","himself","his","how",
    "however","i","if","important","in","interest","interested","interesting","interests","into","is","it","its",
    "itself","j","just","k","keep","keeps","kind","knew","know","known","knows","l","large","largely","last","later",
    "latest","least","less","let","lets","like","likely","long","longer","longest","m","made","make","making","man",
    "many","may","me","member","members","men","might","more","most","mostly","mr","mrs","much","must","my","myself",
    "n","necessary","need","needed","needing","needs","never","new","new","newer","newest","next","no","nobody","non",
    "noone","not","nothing","now","nowhere","number","numbers","o","of","off","often","old","older","oldest","on",
    "once","one","only","open","opened","opening","opens","or","order","ordered","ordering","orders","other","others"
    ,"our","out","over","p","part","parted","parting","parts","per","perhaps","place","places","point","pointed",
    "pointing","points","possible","present","presented","presenting","presents","problem","problems","put","puts","q",
    "quite","r","rather","really","right","right","room","rooms","s","said","same","saw","say","says","second","seconds",
    "see","seem","seemed","seeming","seems","sees","several","shall","she","should","show","showed","showing","shows","side",
    "sides","since","small","smaller","smallest","so","some","somebody","someone","something","somewhere","state","states",
    "still","still","such","sure","t","take","taken","than","that","the","their","them","then","there","therefore","these",
    "they","thing","things","think","thinks","this","those","though","thought","thoughts","three","through","thus","to",
    "today","together","too","took","toward","turn","turned","turning","turns","two","u","under","until","up","upon","us",
    "use","used","uses","v","very","w","want","wanted","wanting","wants","was","way","ways","we","well","wells","went","were",
    "what","when","where","whether","which","while","who","whole","whose","why","will","with","within","without","work",
    "worked","working","works","would","x","y","year","years","yet","you","young","younger","youngest","your","yours","z","http",
    "www","com","in","org","ca","de","fr","io","it","ht","mr","nl","pt","vo","references","reflist","external" ,"links","br","box",
    "nbsp","blockquote","quot","miles","mile","km","mt","background","color","font","size","align","style","center","height",
    "px","nm","la","mpr","noh","nah","mi","sq","sqmi","ei","id","qr","dp","ds","en","wa","aaaaaa","jpeg","jpg","png","gif","cellpadding"
    ,"cellspacing","amp","inline","dms"
};

static Map<String,Integer> stopmap;
static Map<String,Integer> freqmap;

static
{
	stopmap=new HashMap<String,Integer>();
	freqmap=new HashMap<String,Integer>();
	
	int i;
	for(i=0;i<stop_words.length;i++)stopmap.put(stop_words[i], 1);
	for(i=0;i<freq_words.length;i++)freqmap.put(freq_words[i], 1);
	
}


public boolean isStopWord(String word)
{
	if(stopmap.containsKey(word))return true;
	return false;
}

}
