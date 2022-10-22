using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class SpriteChangerScript : GameManager
{
    [SerializeField] Image image;

    [SerializeField]
    private Sprite[] decolation = new Sprite[10];


    private void deco(int deco_number)
    {
        image.sprite = decolation[deco_number];
    }
}
