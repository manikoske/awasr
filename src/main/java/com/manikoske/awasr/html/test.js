if (typeof(networkAreas) == 'undefined') {

    networkAreas = {};

    networkAreas.SERVICE_HTML_URL = "register.html?";
    networkAreas.REGISTRATION_FRAGMENT = "#registration-block";
    // networkAreas.KMZ_BASE_URL = "http://www.awasr.om/KMZ/";
    // networkAreas.KMZ_BASE_URL = "https://www.dropbox.com/s/ztr5j6tplyryaxu/";
    networkAreas.KMZ_BASE_URL = "https://www.dropbox.com/s/cgpf3ipwm4s92pv/";
    networkAreas.MAP_ELEMENT_ID = "map_canvas";

    // https://www.dropbox.com/s/cgpf3ipwm4s92pv/Al%20Khod.zip?dl=0


    networkAreas.areas = {
        alKoudh : {
            title: "Al-Koudh",
            kmlUrlSuffix: "Al-Khod.zip?dl=1",
            lat: 23.627010,
            lng: 58.206414,
        },
    }

    networkAreas.currentlySelected = networkAreas.areas.alKoudh;

    networkAreas.getKmzUrl = function() {
        return networkAreas.KMZ_BASE_URL + networkAreas.currentlySelected.kmlUrlSuffix;
    }


    networkAreas.initialize = function() {
        var myOptions = {
            zoom: 14,
            center: new google.maps.LatLng(networkAreas.currentlySelected.lat, networkAreas.currentlySelected.lng),
            mapTypeId: google.maps.MapTypeId.SATELLITE
        }
        var map = new google.maps.Map(document.getElementById(networkAreas.MAP_ELEMENT_ID), myOptions);
        var marker = new google.maps.Marker({ map: map });
        var layer = new google.maps.KmlLayer(networkAreas.getKmzUrl(), { preserveViewport: true, suppressInfoWindows: true});
        layer.setMap(map);

        //Resize Function
        google.maps.event.addDomListener(window, "resize", function() {
            var center = map.getCenter();
            google.maps.event.trigger(map, "resize");
            map.setCenter(center);
        });

        google.maps.event.addListener(layer, 'click', function(event) {
            showInContentWindow(event.latLng, event.featureData.description);
        });

        function showInContentWindow(position, text) {
            var content = networkAreas.getPopUpContent(text);

            var infowindow = new google.maps.InfoWindow({
                content: content,
                position: position
            })
            infowindow.open(map);
        }
    }

    networkAreas.getPopUpContent = function (text) {
        var descriptions = text.split("~");
        var fibreHub = descriptions[0];
        var networkPoint = descriptions[1];
        var serial = descriptions[2];
        var tagId = descriptions[3];
        var longitude = descriptions[4];
        var latitude = descriptions[5];

        
        var redirectUrl = networkAreas.SERVICE_HTML_URL + "tagId=" + tagId  + "&district=" + networkAreas.currentlySelected.title + "&long=" + longitude + "&lat=" + latitude +  networkAreas.REGISTRATION_FRAGMENT;
        var content = ""+
            "<table style='font-family:Arial,Verdana,Times;font-size:12px;text-align:left;width:100%;border-spacing:0px; padding:0px 3px 3px 3px'>"+
            "<tr>"+
            "<td>"+
            "<div style='height:45px; width:64px; clear:both; background-image: url(\"images/AwasrTinyLogo.jpg\");'>"+
            "</div>"+
            "</td>"+
            "</tr>"+
            "<tr>"+
            "<td>Network</td>"+
            "<td>Awasr Network</td>"+
            "<tr>"+
            "<tr style='background:#D4E4F3;'>"+
            "<td>District</td>"+
            "<td>"+networkAreas.currentlySelected.title+"</td>"+
            "</tr>"+
            "<tr>"+
            "<td>Longitude</td>"+
            "<td>"+longitude+"</td>"+
            "</tr>"+
            "<tr style='background:#D4E4F3;'>"+
            "<td>Latitude</td>"+
            "<td>"+latitude+"</td>"+
            "</tr>"+
            "<tr>"+
            "<td>Fibre Hub</td>"+
            "<td>"+fibreHub+"</td>"+
            "</tr>"+
            "<tr style='background:#D4E4F3;'>"+
            "<td>Network Point</td>"+
            "<td>"+networkPoint+"</td>"+
            "</tr>"+
            "<tr>"+
            "<td>Serial</td>"+
            "<td>"+serial+"</td>"+
            "</tr>"+
            "<tr style='background:#D4E4F3;'>"+
            "<td>TAG ID</td>"+
            "<td><b>"+tagId+"</b></td>"+
            "</tr>"+
            "<tr>"+
            "<td colspan='2' style='padding-top: 10px;'>Is this your home?</td>"+
            "</tr>"+
            "<tr>"+
            "<td colspan='2' style='text-align: right;'>"+
            "<a target='_blank' href='"+redirectUrl+"'>Order now</a>"+
            "</td>"+
            "</tr>"+
            "</table>";
        return content;
    }
}

//var TITLE_AL_MAWALEH_PHASE_1 = "Al-Mawaleh";
//var TITLE_AL_MAWALEH_PHASE_2 = "Al-Mawaleh South";
//var TITLE_AL_HAIL = "Al-Hail";
//var TITLE_AL_MAABILAH_WEST = "Al-Maabilah West";
//var TITLE_AL_MAABILAH_SOUTH = "Al-Maabilah South";
//var TITLE_AL_MAABILAH_NORTH = "Al-Maabilah North";
//var TITLE_AL_QURUM = "Shati-Al-Qurum";



//var KMZ_URLS = {};
//KMZ_URLS[TITLE_AL_KOUDH] = KMZ_BASE_URL+"Al-Koudh.zip";
//KMZ_URLS[TITLE_AL_MAWALEH_PHASE_1] = KMZ_BASE_URL+"mawalih-south-phase1.zip";
//KMZ_URLS[TITLE_AL_MAWALEH_PHASE_2] = KMZ_BASE_URL+"mawalih-south-phase2.zip";
//KMZ_URLS[TITLE_AL_HAIL] = KMZ_BASE_URL+"Al-Hail.zip";
//KMZ_URLS[TITLE_AL_MAABILAH_WEST] = KMZ_BASE_URL+"Al-Maabilah-West.zip";
//KMZ_URLS[TITLE_AL_MAABILAH_SOUTH] = KMZ_BASE_URL+"Al-Maabilah-South.zip";
//KMZ_URLS[TITLE_AL_MAABILAH_NORTH] = KMZ_BASE_URL+"Al-Maabilah-North.zip";
//KMZ_URLS[TITLE_AL_QURUM] = KMZ_BASE_URL+"Shati-Al-Qurum.zip";

